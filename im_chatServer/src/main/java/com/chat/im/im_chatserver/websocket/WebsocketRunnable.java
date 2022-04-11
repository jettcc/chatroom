package com.chat.im.im_chatserver.websocket;

import com.chat.im.im_common.entity.entity.MessageRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

/**
 * channel中任务队列的线程任务
 *
 * @author: chovychan in 2022/4/11
 */
@Log4j2
public class WebsocketRunnable implements Runnable {

    private ChannelHandlerContext channelHandlerContext;

    private MessageRequest messageRequest;

    public WebsocketRunnable(ChannelHandlerContext channelHandlerContext, MessageRequest messageRequest) {
        this.channelHandlerContext = channelHandlerContext;
        this.messageRequest = messageRequest;
    }

    @Override
    public void run() {
        try {
            log.info(Thread.currentThread().getName() + "--" + LocalDateTime.now());
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(LocalDateTime.now().toString()));
        } catch (Exception e) {
            log.error("websocket服务器推送消息发生错误：", e);
        }
    }
}
