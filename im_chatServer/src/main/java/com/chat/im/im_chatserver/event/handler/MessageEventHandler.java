package com.chat.im.im_chatserver.event.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.im.im_chatserver.component.LogMapper;
import com.chat.im.im_chatserver.component.RedisMapper;
import com.chat.im.im_chatserver.component.SourceMapper;
import com.chat.im.im_chatserver.config.chat.ChatHandler;
import com.chat.im.im_chatserver.config.chat.UserChannelRel;
import com.chat.im.im_chatserver.event.MessageEvent;
import com.chat.im.im_chatserver.utils.CommonUtils;
import com.chat.im.im_chatserver.utils.JsonUtils;
import com.chat.im.im_common.entity.dto.UserGroup;
import com.chat.im.im_common.entity.dto.UserUser;
import com.chat.im.im_common.entity.entity.BaseUser;
import com.chat.im.im_common.entity.entity.Group;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import com.chat.im.im_common.entity.enumeration.Role;
import com.chat.im.im_common.mapper.GroupMapper;
import com.chat.im.im_common.mapper.MessageMapper;
import com.chat.im.im_common.mapper.UserGroupMapper;
import com.chat.im.im_common.mapper.UserUserMapper;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 事件处理
 *
 * @author: chovychan in 2022/5/11
 */
@Service
public class MessageEventHandler implements MessageEvent {
    private static final String TAG = "MessageEventHandler";
    private final RedisMapper redisMapper;
    private final MessageMapper messageMapper;
    private final UserUserMapper userUserMapper;
    private final UserGroupMapper userGroupMapper;
    private final GroupMapper groupMapper;
    private final SourceMapper sourceMapper;
    private final ChannelGroup channels = ChatHandler.channels;

    public MessageEventHandler(RedisMapper redisMapper, MessageMapper messageMapper, UserUserMapper userUserMapper, UserGroupMapper userGroupMapper, GroupMapper groupMapper, SourceMapper sourceMapper) {
        this.redisMapper = redisMapper;
        this.messageMapper = messageMapper;
        this.userUserMapper = userUserMapper;
        this.userGroupMapper = userGroupMapper;
        this.groupMapper = groupMapper;
        this.sourceMapper = sourceMapper;
    }

    @Getter
    enum InviteType {
        FRIEND("邀请你成为好友"),
        GROUP("邀请你加入群聊");
        final String name;

        InviteType(String name) {
            this.name = name;
        }
    }

    @Override
    public void event(Message message, Channel channel) {
        var msgType = message.getMsgType();
        if (!check(message)) throw LogMapper.error(TAG, "禁言中不能发言");
        MessageInterface messageService = new MessageInterfaceImpl();
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
            case KEEPALIVE -> LogMapper.info(TAG, "收到[" + channel + "]的心跳请求...");
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
                QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
                wrapper.eq("group_id", groupId);
                List<String> ids = userGroupMapper.selectList(wrapper).stream()
                        .map(UserGroup::getUserId).toList();
                Message saveMsg = messageService.saveMsg(message);
                for (String toId : ids) {
                    pushMsg(toId, saveMsg);
                }
            }
            case JOIN_GROUP -> {
                // 加入群聊的逻辑是通知管理员审核, 审核通过就加入, 此时应该有个事件
                String uid = message.getFromId(); //
                String gid = message.getToId(); // 群组id
                // 找到群的管理员或者群主, 这里为了方便先找管理员
                QueryWrapper<UserGroup> qw = new QueryWrapper<>();
                List<String> userGroups =
                        userGroupMapper.selectList(qw.eq("group_id", gid)
                                        .eq("role", Role.ADMIN.getName())).stream()
                                .map(UserGroup::getUserId).collect(Collectors.toList());
                // 找到管理员了之后向他们推送消息, 在线的管理员直接推送, 不在线的管理员存离线消息
                Message joinMsg = new Message()
                        .setMsgType(MsgEnum.NOTICE)
                        .setFromId(uid)
                        .setMsgContext("申请加入群聊")
                        .setToId(gid);
                for (String id : userGroups) {
                    pushMsg(id, joinMsg);
                }
            }
            case INVITE -> {
                // 邀请事件比较简单, push过去就行了, 区分好友和群组邀请
                String formId = message.getFromId();
                String toId = message.getToId();
                InviteType type = InviteType.valueOf(message.getMsgContext());
                BaseUser u = findUser(formId);
                Message notice = new Message().setFromId(formId).setToId(toId)
                        .setMsgType(MsgEnum.NOTICE)
                        .setMsgContext("[" + u.getNickName() + "] " + type.getName());
                pushMsg(toId, notice);
            }
            case AUDIT -> {
                // 审核事件, 有好友和审核入群两种事件
                String toId = message.getToId();
                String fromId = message.getFromId();
                InviteType type = InviteType.valueOf(message.getMsgContext());
                switch (type) {
                    case GROUP -> // 审核入群的事件就绑定一条记录就行
                            userGroupMapper.insert(new UserGroup().setUserId(fromId).setGroupId(Long.valueOf(toId)));
                    case FRIEND -> {
                        // 好友事件比较简单, 插入两条双向关联的记录就行
                        userUserMapper.insert(new UserUser().setUserId(fromId).setFriendId(toId).setMute(false));
                        userUserMapper.insert(new UserUser().setFriendId(fromId).setUserId(toId).setMute(false));
                    }
                }
            }

        }
    }

    private boolean check(Message message) {
        return switch (message.getMsgType()) {
            case GROUP -> {
                String fromId = message.getFromId();
                Long toId = Long.valueOf(message.getToId());
                Group group = groupMapper.selectById(toId);
                QueryWrapper<UserGroup> qw = new QueryWrapper<>();
                qw.eq("user_id", fromId);
                qw.eq("group_id", toId);
                UserGroup userGroup = Optional.ofNullable(userGroupMapper.selectOne(qw))
                        .orElseThrow(() -> LogMapper.error(TAG, "该用户不在此群聊中, 无法发送消息"));
                yield !userGroup.getMute() || !group.getMute();
            }
            case CHAT -> {
                // todo
                yield true;
            }
            default -> true;
        };
    }

    private void pushMsg(String toId, Message message) {
        switch (message.getMsgType()) {
            case CHAT, NOTICE -> {
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
            case GROUP -> {
                Channel toUserChannel = UserChannelRel.get(toId);
                // 群聊消息不打算做已读未读, 所以这里直接找到在线的就push, 不在线的就算了
                if (toUserChannel == null || channels.find(toUserChannel.id()) == null) return;
                toUserChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(message)));
            }
        }
    }

    private BaseUser findUser(String uid) {
        return sourceMapper.findUser(TAG, "", uid);
    }

    interface MessageInterface extends IService<Message> {
        Message saveMsg(Message message);

        void readMsg(List<Long> ids);
    }

    @Service
    class MessageInterfaceImpl extends ServiceImpl<MessageMapper, Message> implements MessageInterface {
        {
            //初始化
            baseMapper = messageMapper;
        }

        @Override
        public Message saveMsg(Message message) {
            this.save(message);
            return message;
        }

        @Override
        public void readMsg(List<Long> ids) {
            Message message = new Message().setHaveRead(true);
            UpdateWrapper<Message> qw = new UpdateWrapper<>();
            qw.in("id", ids);
            this.update(message, qw);
        }
    }
}
