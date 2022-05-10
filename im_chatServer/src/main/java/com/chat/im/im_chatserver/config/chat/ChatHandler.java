package com.chat.im.im_chatserver.config.chat;

import com.chat.im.im_chatserver.component.LogMapper;
import com.chat.im.im_chatserver.service.MessageService;
import com.chat.im.im_chatserver.service.impl.MessageServiceImpl;
import com.chat.im.im_chatserver.utils.JsonUtils;
import com.chat.im.im_chatserver.utils.SpringUtil;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 聊天处理
 *
 * @author: chovychan in 2022/5/8
 */
@Log4j2
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final String TAG = "ChatHandler";
    // 用于记录和管理所有客户端的channel
    private static final ChannelGroup channels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        Channel currentChannel = ctx.channel();
        // 1. 获取客户端发来的消息
        Message message = Optional.ofNullable(JsonUtils.jsonToPojo(content, Message.class))
                .orElseThrow(() -> {
                    throw LogMapper.error(TAG, "jsonToPojo 映射错误, 请检查");
                });
        log.info("接受到的数据：" + content);
        MsgEnum msgType = message.getMsgType();
        if (Objects.isNull(msgType)) throw LogMapper.error(TAG, "错误, 消息类型不能为空");
        switch (msgType) {
            // 链接
            case CONNECT -> {
                // 当websocket 第一次open的时候，初始化channel，把用的channel和发送方id关联起来
                UserChannelRel.put(message.getFromId(), currentChannel);
                // 测试
                channels.stream().map(Channel::id).map(ChannelId::asLongText).forEach(log::info);
                UserChannelRel.output();
            }
            case CHAT -> {
                //  2.2  聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
                String msgContext = message.getMsgContext();
                String toId = message.getToId();
                String formId = message.getFromId();
                // 保存消息到数据库，并且标记为未签收
                MessageService messageService = SpringUtil.getBean(MessageServiceImpl.class);
                Message saveMsg = messageService.saveMsg(message);

//                DataContent dataContentMsg = new DataContent();
//                dataContentMsg.setChatMsg(chatMsg);
                // 发送消息
                // 从全局用户Channel关系中获取接受方的channel
                Channel receiverChannel = UserChannelRel.get(toId);
                if (receiverChannel == null) {
                    // todo 没有这个用户
                    // channel为空代表用户离线，推送消息, 这里的推送暂时做不了
                } else {
                    // 当receiverChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
                    Channel findChannel = channels.find(receiverChannel.id());
                    if (findChannel != null) {
                        // 用户在线
                        receiverChannel.writeAndFlush(
                                new TextWebSocketFrame(
                                        JsonUtils.objectToJson(saveMsg)));
                    } else {
                        // 用户离线 TODO 推送消息
                    }
                }

            }
        }

        System.out.println(message);
        // 获取客户端传输过来的消息
//        String content = msg.text();

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
