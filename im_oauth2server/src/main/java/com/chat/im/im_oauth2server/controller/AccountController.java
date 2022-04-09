package com.chat.im.im_oauth2server.controller;

import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import com.chat.im.im_oauth2server.dto.GetUserTokenDTO;
import com.chat.im.im_oauth2server.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Keyvonchen in 2021/9/15
 */
@Api(tags = "api- 权限认证")
@RestController
@RequestMapping("/auth/account")
public class AccountController {
    private final AccountService accountService;

    private final static String OAUTH_URL = "http://localhost:1225/oauth/check_token";

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录接口", httpMethod = "POST")
    public SystemMsgJsonResponse login(@RequestBody @Valid @Validated GetUserTokenDTO dto,
                                       @RequestHeader HttpHeaders httpHeaders) {
        var openId = accountService.wxcodeLogin(dto);
        return SystemMsgJsonResponse.success(new HashMap<>() {{
            put("access_token", Optional.ofNullable(accountService.request(openId, httpHeaders))
                    .orElseThrow(() -> new RuntimeException("access_token 获取失败"))
                    .getString("access_token"));
        }});
    }
}
