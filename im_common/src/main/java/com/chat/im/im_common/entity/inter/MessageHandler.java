package com.chat.im.im_common.entity.inter;

import io.netty.channel.Channel;
/**
 * @author: chovychan in 2022/4/27
 */
public interface MessageHandler<T extends Message> {

    /**
     * 执行处理消息
     *
     * @param channel 通道
     * @param message 消息
     */
    void execute(Channel channel, T message);

    /**
     * @return 消息类型，即每个 Message 实现类上的 TYPE 静态字段
     */
    String getType();

}