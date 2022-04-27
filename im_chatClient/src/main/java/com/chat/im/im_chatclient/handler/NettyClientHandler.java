package com.chat.im.im_chatclient.handler;

import com.chat.im.im_chatclient.netty.NettyClient;
import com.chat.im.im_common.entity.entity.Invocation;
import com.chat.im.im_common.hearbeat.HeartbeatRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Log4j2
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private NettyClient nettyClient;

    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 发起重连
        nettyClient.reconnect();
        // 继续触发事件
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // 空闲时，向服务端发起一次心跳
        if (event instanceof IdleStateEvent) {
            log.info("[客户端] - [发起一次心跳]");
            HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            ctx.writeAndFlush(new Invocation(HeartbeatRequest.TYPE, heartbeatRequest))
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, event);
        }
    }

}