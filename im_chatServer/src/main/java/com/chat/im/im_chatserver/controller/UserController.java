package com.chat.im.im_chatserver.controller;

import com.chat.im.im_chatserver.component.AuthenticationInfo;
import com.chat.im.im_chatserver.dto.account.SetInfoDTO;
import com.chat.im.im_chatserver.service.AccountService;
import com.chat.im.im_chatserver.vo.account.UserInfoVO;
import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: chovychan in 2022/5/8
 */
@Api(tags = "api - 用户模块")
@RestController
@RequestMapping("/chat/account")
public class UserController {
    private final AuthenticationInfo auth;
    private final AccountService accountService;

    public UserController(AuthenticationInfo auth, AccountService accountService) {
        this.auth = auth;
        this.accountService = accountService;
    }

    @GetMapping("/info")
    @ApiOperation(value = "[用户模块] - 查询用户信息", httpMethod = "GET", response = UserInfoVO.class)
    public SystemMsgJsonResponse info() {
        return SystemMsgJsonResponse.success(accountService.info(auth.getId()));
    }

    @PostMapping("/set")
    @ApiOperation(value = "[用户模块] - 修改个人信息", httpMethod = "POST")
    public SystemMsgJsonResponse set(@Valid SetInfoDTO dto) {
        accountService.set(auth.getId(), dto);
        return SystemMsgJsonResponse.success();
    }
}
