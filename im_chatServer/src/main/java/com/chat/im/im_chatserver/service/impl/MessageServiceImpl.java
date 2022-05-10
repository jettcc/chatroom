package com.chat.im.im_chatserver.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.im.im_chatserver.service.MessageService;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * @author: chovychan in 2022/5/10
 */
@Service(value = "messageServiceImpl")
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public Message saveMsg(Message message) {
        MessageInterface messageInterface = new MessageInterfaceImpl();
        messageInterface.save(message);
        return message;
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
