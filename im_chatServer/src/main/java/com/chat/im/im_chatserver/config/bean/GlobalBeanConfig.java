package com.chat.im.im_chatserver.config.bean;

import com.chat.im.im_chatserver.handler.NettyServerHandler;
import com.chat.im.im_common.entity.inter.MessageDispatcher;
import com.chat.im.im_common.entity.inter.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 全局bean 管理
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
