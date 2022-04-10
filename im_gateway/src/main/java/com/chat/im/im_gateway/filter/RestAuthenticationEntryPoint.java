package com.chat.im.im_gateway.filter;

import com.achobeta.dgut.ab_common.entity.enumeration.GlobalServiceMsgCode;
import com.chat.im.im_gateway.utils.RespUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 未认证策略管理器
 * @author: AubreyChen in 2022/3/7
 */
@Component
@Log4j2
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        log.error("<< Gateway::RestAuthenticationEntryPoint::AUTH >>: 没有认证, info: [{}]", e.getMessage());
        return RespUtils.createAccessDeniedResponse(exchange.getResponse(), GlobalServiceMsgCode.USER_ACCOUNT_EXPIRED);
    }
}
