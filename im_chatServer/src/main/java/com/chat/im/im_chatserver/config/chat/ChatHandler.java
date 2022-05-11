package com.chat.im.im_chatserver.config.chat;

import com.chat.im.im_chatserver.component.LogMapper;
import com.chat.im.im_chatserver.event.MessageEvent;
import com.chat.im.im_chatserver.event.handler.MessageEventHandler;
import com.chat.im.im_chatserver.utils.JsonUtils;
import com.chat.im.im_chatserver.utils.SpringUtil;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 聊天处理
 *
 * @author: chovychan in 2022/5/8
 */
@Log4j2
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final String TAG = "ChatHandler";
    // 用于记录和管理所有客户端的channel
    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        Channel currentChannel = ctx.channel();
        // 1. 获取客户端发来的消息
        Message message = Optional.ofNullable(JsonUtils.jsonToPojo(content, Message.class))
                .orElseThrow(() -> {
                    throw LogMapper.error(TAG, "jsonToPojo 映射错误, 请检查");
                });
        log.info("接受到的数据: {}, message: {}", content, message);
        MsgEnum msgType = message.getMsgType();
        if (Objects.isNull(msgType)) throw LogMapper.error(TAG, "错误, 消息类型不能为空");
        MessageEvent messageEvent = SpringUtil.getBean(MessageEventHandler.class);
        // 事件处理
        messageEvent.event(message, currentChannel);

        // 获取客户端传输过来的消息
//        String content = msg.text();

        channels.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接受到消息, 消息为：" + content));
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("一个客户端链接...");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        channels.remove(ctx.channel());
        log.info("客户端断开，channel对应的长id为：{}, 短id为: {}",
                ctx.channel().id().asLongText(),
                ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        log.error("客户端异常, error msg: {}", e.getMessage());
        ctx.channel().close();
        channels.remove(ctx.channel());
    }

    public ChannelGroup getChannels() {
        return channels;
    }
}
