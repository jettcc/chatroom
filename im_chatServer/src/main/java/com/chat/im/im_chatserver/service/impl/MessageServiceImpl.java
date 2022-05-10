package com.chat.im.im_chatserver.service.impl;

import com.alibaba.nacos.client.utils.LogUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.im.im_chatserver.component.LogMapper;
import com.chat.im.im_chatserver.component.RedisMapper;
import com.chat.im.im_chatserver.service.MessageService;
import com.chat.im.im_chatserver.utils.CommonUtils;
import com.chat.im.im_chatserver.vo.message.GetUnreadMessageVO;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chovychan in 2022/5/10
 */
@Service(value = "messageServiceImpl")
public class MessageServiceImpl implements MessageService {
    private static final String TAG = "MessageServiceImpl";
    private final MessageMapper messageMapper;
    private final RedisMapper redisMapper;

    public MessageServiceImpl(MessageMapper messageMapper, RedisMapper redisMapper) {
        this.messageMapper = messageMapper;
        this.redisMapper = redisMapper;
    }

    @Override
    public Message saveMsg(Message message) {
        MessageInterface messageInterface = new MessageInterfaceImpl();
        messageInterface.save(message);
        return message;
    }

    @Override
    public void readMsg(List<Long> ids) {
        MessageInterface messageInterface = new MessageInterfaceImpl();
        Message message = new Message().setHaveRead(true);
        UpdateWrapper<Message> qw = new UpdateWrapper<>();
        qw.in("id", ids);
        messageInterface.update(message, qw);
    }

    @Override
    public List<GetUnreadMessageVO> getUnreadMsg(String uid) {
        List<Object> unreadMessages = redisMapper.lGet(CommonUtils.encodeStr(uid), 0, -1);
        if (unreadMessages == null) throw LogMapper.error(TAG, "没有未读消息");
        return unreadMessages.stream()
                .map(obj -> (Message) obj).map(GetUnreadMessageVO::new).toList();
    }


    interface MessageInterface extends IService<Message> {

    }

    @Service
    class MessageInterfaceImpl extends ServiceImpl<MessageMapper, Message> implements MessageInterface {
        {
            //初始化
            baseMapper = messageMapper;
        }
    }
}
