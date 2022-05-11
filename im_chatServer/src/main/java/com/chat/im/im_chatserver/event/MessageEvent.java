package com.chat.im.im_chatserver.event;

import com.chat.im.im_common.entity.entity.Message;
import io.netty.channel.Channel;

public interface MessageEvent {

    void event(Message message, Channel channel);
}
