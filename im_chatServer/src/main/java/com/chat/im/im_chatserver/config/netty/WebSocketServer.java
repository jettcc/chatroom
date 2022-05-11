package com.chat.im.im_chatserver.config.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * websocket启动器
 *
 * @author: chovychan in 2022/5/8
 */
@Log4j2
@Component
public class WebSocketServer implements Runnable {
    private static final String TAG = "WebSocketServer";
    private static final int port = 4302;

    @Override
    public void run() {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketInitializer());
            ChannelFuture future = server.bind(port).sync();
            log.info("WebSocket Server run in {}..", port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(TAG + "ERROR: {}", e.getMessage());
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
