package com.chat.im.im_chatclient.netty;

import com.chat.im.im_chatclient.handler.NettyClientHandlerInitializer;
import com.chat.im.im_common.entity.entity.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
/**
 * 客户端启动服务
 * @author: chovychan in 2022/4/27
 */
@Component
@Log4j2
public class NettyClient {

    /**
     * 重连频率，单位：秒
     */
    private static final Integer RECONNECT_SECONDS = 20;


    @Value("${netty.server.host}")
    private String serverHost;
    @Value("${netty.server.port}")
    private Integer serverPort;
    @Autowired
    private NettyClientHandlerInitializer nettyClientHandlerInitializer;

    /**
     * 线程组，用于客户端对服务端的连接、数据读写
     */
    private final EventLoopGroup eventGroup = new NioEventLoopGroup();
    /**
     * Netty Client Channel
     */
    private volatile Channel channel;

    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 创建 Bootstrap 对象，用于 Netty Client 启动
        Bootstrap bootstrap = new Bootstrap();
        //
        bootstrap.group(eventGroup)// 设置一个 EventLoopGroup 对象
                .channel(NioSocketChannel.class) // 指定 Channel 为客户端 NioSocketChannel
                .remoteAddress(serverHost, serverPort)// 指定连接服务器的地址
                .option(ChannelOption.SO_KEEPALIVE, true)// TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .option(ChannelOption.TCP_NODELAY, true) ////  允许较小的数据包的发送，降低延迟
                .handler(nettyClientHandlerInitializer);
        // 连接服务器，并异步等待成功，即启动客户端
        bootstrap.connect().addListener((ChannelFutureListener) future -> {
            // 连接失败
            if (!future.isSuccess()) {
                log.error("[start][Netty Client 连接服务器({}:{}) 失败]", serverHost, serverPort);
                reconnect();
                return;
            }
            //连接成功
            channel = future.channel();
            log.info("[start][Netty Client 连接服务器({}:{}) 成功]", serverHost, serverPort);
        });
    }

    /**
     * 重连
     */
    public void reconnect() {
        eventGroup.schedule(() -> {
            log.info("[reconnect][开始重连]");
            try {
                start();
            } catch (InterruptedException e) {
                log.error("[reconnect][重连失败]", e);
            }
        }, RECONNECT_SECONDS, TimeUnit.SECONDS);
        log.info("[reconnect][{} 秒后将发起重连]", RECONNECT_SECONDS);
    }

    /**
     * 关闭 Netty Server
     */
    @PreDestroy
    public void shutdown() {
        // 关闭 Netty Client
        if (channel != null) {
            channel.close();
        }
        // 优雅关闭一个 EventLoopGroup 对象
        eventGroup.shutdownGracefully();
    }

    /**
     * 发送消息
     *
     * @param invocation 消息体
     */
    public void send(Invocation invocation) {
        if (channel == null) {
            log.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        // 发送消息
        channel.writeAndFlush(invocation);
    }

}