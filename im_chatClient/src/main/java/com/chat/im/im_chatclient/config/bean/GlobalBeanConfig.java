package com.chat.im.im_chatclient.config.bean;

import com.chat.im.im_common.entity.inter.MessageDispatcher;
import com.chat.im.im_common.entity.inter.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author: chovychan in 2022/4/27
 */
@Configuration
public class GlobalBeanConfig {
    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }
}
