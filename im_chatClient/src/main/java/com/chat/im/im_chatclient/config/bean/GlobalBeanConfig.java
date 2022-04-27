package com.chat.im.im_chatclient.config.bean;

import com.chat.im.im_common.entity.inter.MessageDispatcher;
import com.chat.im.im_common.entity.inter.MessageHandlerContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: chovychan in 2022/4/27
 */
@Configuration
public class GlobalBeanConfig {
    private final long tryNum = 3L;
    private final long tryTime = 1000L;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //用UTF-8 StringHttpMessageConverter替换默认StringHttpMessageConverter
        List<HttpMessageConverter<?>> newMessageConverters = new ArrayList<>();
        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                // 默认的是ios 8859-1
                StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
                newMessageConverters.add(messageConverter);
            } else {
                newMessageConverters.add(converter);
            }
        }
        restTemplate.setMessageConverters(newMessageConverters);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        // 只有io异常才会触发重试
        httpClientBuilder.setRetryHandler((exception, curRetryCount, context) -> {
            // curRetryCount 每一次都会递增，从1开始
            if (curRetryCount > tryNum) {
                return false;
            }
            try {
                //重试延迟
                Thread.sleep(curRetryCount * tryTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (exception instanceof NoHttpResponseException || exception instanceof ConnectException) {
                return true;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            org.apache.http.HttpRequest request = clientContext.getRequest();
            // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
            return !(request instanceof HttpEntityEnclosingRequest);
        }).setMaxConnTotal(400);

        // httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(30000);
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(30000);
        // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(10000);
        // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        clientHttpRequestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public RequestContextFilter requestContextFilter() {
        RequestContextFilter requestContextFilter = new RequestContextFilter();
        requestContextFilter.setThreadContextInheritable(true);
        return requestContextFilter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder() // or different mapper for other format
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
    }

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }
}
