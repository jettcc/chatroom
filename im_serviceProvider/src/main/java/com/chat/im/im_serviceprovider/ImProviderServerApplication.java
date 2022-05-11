package com.chat.im.im_serviceprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * @author: chovychan in 2022/5/11
 */
@SpringBootApplication
@MapperScan("com.chat.im.im_common.mapper")
@ComponentScan(basePackages = {"com.chat.im.im_common","com.chat.im.im_serviceprovider"})
public class ImProviderServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImProviderServerApplication.class, args);
    }

}
