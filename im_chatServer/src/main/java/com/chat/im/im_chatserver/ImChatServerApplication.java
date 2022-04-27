package com.chat.im.im_chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 第一个问题：客户端和服务端单独通信，怎么实现？
 *
 * 第二个问题：单机中websocket主动向所有客户端推送消息如何实现？在集群中如何实现？
 *
 * 第三个问题：单机如何统计同时在线的客户数量？websocket集群如何统计在线的客户数量呢？
 *
 * 第四个问题：由于客户端和websocket服务器集群中的某个节点建立长连接是随机的，如何解决服务端向某个或某些部分客户端推送消息？
 *
 * 第五个问题：websocket服务端周期性向客户端推送消息，单机或集群中如何实现？
 *
 * 第六个问题：websocket集群中一个客户端向其他客户端主动发送消息，如何实现？
 *
 * @author: chovychan in 2022/4/11
 */
@SpringBootApplication
public class ImChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImChatServerApplication.class, args);
    }

}
