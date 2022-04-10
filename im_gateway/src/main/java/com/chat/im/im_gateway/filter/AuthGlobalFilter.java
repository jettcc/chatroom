package com.chat.im.im_gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.nimbusds.jose.JWSObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
/**
 * 全局拦截器
 * @author: chovychan in 2022/4/11
 */
@Component
@Log4j2
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }
        try {
            //从token中解析用户信息并设置到Header中去
            String userJsonString = JWSObject
                    .parse(token.replace("Bearer ", "")).getPayload().toString();
            if (StringUtils.isEmpty(userJsonString)) {
                log.error("<< GateWay::AuthGlobalFilter::AUTH >> -- token解析错误, 请检查");
                throw new RuntimeException("token解析错误, 请检查");
            }
//            log.info("<< GateWay::AuthGlobalFilter::AUTH >> ---  user:[{}]", userJsonString);
            ServerHttpRequest request = exchange.getRequest()
                    .mutate().header("user", userJsonString)
                    .build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            log.error("<< GateWay::AuthGlobalFilter::AUTH >> -- ParseException: {}", e.getMessage());
            e.printStackTrace();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
