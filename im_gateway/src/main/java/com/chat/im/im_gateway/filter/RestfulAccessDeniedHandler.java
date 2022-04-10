package com.chat.im.im_gateway.filter;

import com.achobeta.dgut.ab_common.entity.enumeration.GlobalServiceMsgCode;
import com.chat.im.im_gateway.utils.RespUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 未授权策略管理器
 *
 * @author: AubreyChen in 2022/3/7
 */
@Component
@Log4j2
public class RestfulAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("<< Gateway::RestAuthenticationEntryPoint::AUTH >> error: 没有授权, info: [{}]", denied.getMessage());
        return RespUtils.createAccessDeniedResponse(exchange.getResponse(), GlobalServiceMsgCode.USER_NO_PERMISSION);
    }
}
