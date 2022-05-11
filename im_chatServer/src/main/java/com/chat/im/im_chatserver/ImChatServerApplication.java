package com.chat.im.im_chatserver;

import com.chat.im.im_chatserver.config.netty.WebSocketServer;
import com.chat.im.im_chatserver.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.chat.im.im_common.mapper")
@ComponentScan(basePackages = {"com.chat.im.im_common", "com.chat.im.im_chatserver"})
public class ImChatServerApplication {
    @Bean
    public SpringUtil getSpringUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ImChatServerApplication.class, args);
        ExecutorService service = Executors.newCachedThreadPool();
        WebSocketServer webSocketServer = applicationContext.getBean(WebSocketServer.class);
        service.execute(webSocketServer);
        service.shutdown();
    }

}
