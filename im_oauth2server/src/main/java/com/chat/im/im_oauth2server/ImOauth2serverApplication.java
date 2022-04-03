package com.chat.im.im_oauth2server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableOpenApi
@MapperScan(basePackages = "com.achobeta.dgut.ab_common.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.chat.im.im_common", "com.chat.im.im_oauth2server"})
@EnableScheduling
public class ImOauth2serverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImOauth2serverApplication.class, args);
    }

}
