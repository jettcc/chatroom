package com.chat.im.im_chatserver.service;

import com.chat.im.im_common.entity.entity.Message;

public interface MessageService {
    Message saveMsg(Message message);
}
