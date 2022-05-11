package com.chat.im.im_chatserver.config.heatbeat;

import com.chat.im.im_chatserver.config.chat.ChatHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;
/**
 * 心跳处理
 * @author: chovychan in 2022/5/11
 */
@Log4j2
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                log.info("进入读空闲...");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("进入写空闲...");
            } else if (event.state() == IdleState.ALL_IDLE) {
//                log.info("channel关闭前，users的数量为：" + ChatHandler.channels.size());
                // 关闭无用的channel，以防资源浪费
                ctx.channel().close();
                log.info("channel关闭后，users的数量为：" + ChatHandler.channels.size());
            }
        }

    }

}