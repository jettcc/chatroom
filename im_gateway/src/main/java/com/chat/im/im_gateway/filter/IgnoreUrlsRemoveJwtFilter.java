package com.chat.im.im_gateway.filter;

import com.chat.im.im_gateway.config.IgnoreUrlsConfig;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 白名单策略
 * @author: chovychan in 2022/4/11
 */
@Component
@Log4j2
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {
    @Autowired
    IgnoreUrlsConfig ignoreUrlsConfig;

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        String methodValue = request.getMethodValue();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径移除JWT请求头
        var list = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : list) {
            if (pathMatcher.match(ignoreUrl, path) &&
                    ("GET".equalsIgnoreCase(methodValue) ||
                            "POST".equalsIgnoreCase(methodValue)) &&
                    (token == null || Objects.equals(token, ""))) {
                request = exchange.getRequest().mutate().header("Authorization", "").build();
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }
}

