package com.chat.im.im_chatserver.event.handler;

import com.chat.im.im_chatserver.component.LogMapper;
import com.chat.im.im_chatserver.component.RedisMapper;
import com.chat.im.im_chatserver.config.chat.ChatHandler;
import com.chat.im.im_chatserver.config.chat.UserChannelRel;
import com.chat.im.im_chatserver.event.MessageEvent;
import com.chat.im.im_chatserver.service.GroupService;
import com.chat.im.im_chatserver.service.MessageService;
import com.chat.im.im_chatserver.utils.CommonUtils;
import com.chat.im.im_chatserver.utils.JsonUtils;
import com.chat.im.im_chatserver.vo.account.UserInfoVO;
import com.chat.im.im_common.entity.entity.Message;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 事件处理
 *
 * @author: chovychan in 2022/5/11
 */
@Service
public class MessageEventHandler implements MessageEvent {
    private static final String TAG = "MessageEventHandler";
    private final RedisMapper redisMapper;
    private final MessageService messageService;
    private final GroupService groupService;
    private final ChannelGroup channels = ChatHandler.channels;

    public MessageEventHandler(RedisMapper redisMapper, MessageService messageService, GroupService groupService) {
        this.redisMapper = redisMapper;
        this.messageService = messageService;
        this.groupService = groupService;
    }

    @Override
    public void event(Message message, Channel channel) {
        var msgType = message.getMsgType();
        switch (msgType) {
            // 链接
            case CONNECT -> {
                // 当websocket 第一次open的时候，初始化channel，把用的channel和发送方id关联起来
                UserChannelRel.put(message.getFromId(), channel);
                UserChannelRel.output();
            }
            case CHAT -> {
                String toId = message.getToId();
                // 保存消息到数据库，并且标记为未签收
                Message saveMsg = messageService.saveMsg(message);
                // 发送消息
                // 从全局用户Channel关系中获取接受方的channel
                pushMsg(toId, saveMsg);
            }
            case SIGNED -> { // 将消息标记为已读
                // 这里规定好, 当事件为SIGNED, msgContext就是msg的id, 用 "," 分隔
                List<Long> msgIds = Arrays.stream(message.getMsgContext().split(","))
                        .map(Long::valueOf).toList();
                messageService.readMsg(msgIds);
            }
            case KEEPALIVE -> {
                LogMapper.info(TAG, "收到[" + channel + "]的心跳请求...");
            }
            case UNREAD -> {
                String key = CommonUtils.encodeStr(message.getFromId());
                List<Object> unreadMessages = redisMapper.lGet(key, 0, -1);
                if (unreadMessages == null) throw LogMapper.error(TAG, "没有未读消息");
                redisMapper.del(key);
                unreadMessages.stream().map(obj -> (Message) obj).map(JsonUtils::objectToJson)
                        .map(TextWebSocketFrame::new).forEach(channels::writeAndFlush);
            }
            case GROUP -> {
                // 这里约定好toId是群组Id
                Long groupId = Long.valueOf(message.getToId());
                List<String> ids = groupService.groupMember(groupId).stream()
                        .map(UserInfoVO::getId).toList();
                Message saveMsg = messageService.saveMsg(message);
                for (String toId : ids) {
                    pushMsg(toId, saveMsg);
                }
            }
        }
    }

    private void pushMsg(String toId, Message message) {
        Channel toUserChannel = UserChannelRel.get(toId);
        if (toUserChannel == null || channels.find(toUserChannel.id()) == null) {
            // todo 离线用户, 将消息缓存到离线库中, 这里用redis存
            // 有两种策略, 要么就是在连接的时候读取, 要么就是用读取事件
            redisMapper.lrpush(CommonUtils.encodeStr(toId), message);
        } else {
            // 用户在线, 此时根据websocket, 用户一定能收到消息
            toUserChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(message)));
        }
    }
}
