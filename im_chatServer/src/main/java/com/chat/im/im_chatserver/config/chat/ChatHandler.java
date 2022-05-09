package com.chat.im.im_chatserver.config.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
/**
 * 聊天处理
 * @author: chovychan in 2022/5/8
 */
@Log4j2
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 用于记录和管理所有客户端的channel
    private static final ChannelGroup channels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        log.info("接受到的数据：" + content);
        channels.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接受到消息, 消息为：" + content));
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("一个客户端链接...");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        channels.remove(ctx.channel());
        log.info("客户端断开，channel对应的长id为：{}, 短id为: {}",
                ctx.channel().id().asLongText(),
                ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        log.error("客户端异常, error msg: {}", e.getMessage());
        ctx.channel().close();
        channels.remove(ctx.channel());
    }
}
