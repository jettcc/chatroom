package com.chat.im.im_chatclient.config.handler;

import com.chat.im.im_common.entity.inter.InvocationDecoder;
import com.chat.im.im_common.entity.inter.InvocationEncoder;
import com.chat.im.im_common.entity.inter.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author: chovychan in 2022/4/27
 */
@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 60;

    private MessageDispatcher messageDispatcher;

    private NettyClientHandler nettyClientHandler;

    @Autowired
    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    @Autowired
    public void setNettyClientHandler(NettyClientHandler nettyClientHandler) {
        this.nettyClientHandler = nettyClientHandler;
    }


    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                // 空闲检测
                .addLast(new IdleStateHandler(READ_TIMEOUT_SECONDS, 0, 0))
                .addLast(new ReadTimeoutHandler(3 * READ_TIMEOUT_SECONDS))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发器
                .addLast(messageDispatcher)
                // 客户端处理器
                .addLast(nettyClientHandler);
    }

}