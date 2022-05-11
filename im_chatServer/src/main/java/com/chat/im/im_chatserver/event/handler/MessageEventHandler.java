package com.chat.im.im_chatserver.event.handler;

import com.chat.im.im_chatserver.component.LogMapper;
import com.chat.im.im_chatserver.component.RedisMapper;
import com.chat.im.im_chatserver.config.chat.ChatHandler;
import com.chat.im.im_chatserver.config.chat.UserChannelRel;
import com.chat.im.im_chatserver.event.MessageEvent;
import com.chat.im.im_chatserver.service.MessageService;
import com.chat.im.im_chatserver.utils.CommonUtils;
import com.chat.im.im_chatserver.utils.JsonUtils;
import com.chat.im.im_common.entity.entity.Message;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MessageEventHandler implements MessageEvent {
    private static final String TAG = "MessageEventHandler";
    private final RedisMapper redisMapper;
    private final MessageService messageService;
    public MessageEventHandler(RedisMapper redisMapper, MessageService messageService) {
        this.redisMapper = redisMapper;
        this.messageService = messageService;
    }


    @Override
    public void event(Message message, Channel channel) {
        var channels = new ChatHandler().getChannels();
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
                Channel receiverChannel = UserChannelRel.get(toId);
                if (receiverChannel == null || channels.find(receiverChannel.id()) == null) {
                    // todo 离线用户, 将消息缓存到离线库中, 这里用redis存
                    // 有两种策略, 要么就是在连接的时候读取, 要么就是用读取事件
                    redisMapper.lrpush(CommonUtils.encodeStr(toId), message);
                } else {
                    // 用户在线, 此时根据websocket, 用户一定能收到消息
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(saveMsg)));
                }
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

        }
    }
}
