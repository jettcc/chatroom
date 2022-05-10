package com.chat.im.im_fileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * @author: chovychan in 2022/5/8
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableDiscoveryClient
public class ImFileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImFileServerApplication.class, args);
    }

}
