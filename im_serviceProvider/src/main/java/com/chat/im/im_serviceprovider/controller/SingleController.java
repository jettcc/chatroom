package com.chat.im.im_serviceprovider.controller;

import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import com.chat.im.im_serviceprovider.component.AuthenticationInfo;
import com.chat.im.im_serviceprovider.dto.single.SelectUserDTO;
import com.chat.im.im_serviceprovider.service.SingleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author: chovychan in 2022/5/11
 */
@Api(tags = "api - 一对一模块")
@RequestMapping("/chat/single")
@RestController
public class SingleController {
    private final AuthenticationInfo auth;
    private final SingleService singleService;

    public SingleController(AuthenticationInfo auth, SingleService singleService) {
        this.auth = auth;
        this.singleService = singleService;
    }

    @GetMapping("/page")
    @ApiOperation(value = "[一对一模块] - 分页查询聊天记录", httpMethod = "GET")
    public SystemMsgJsonResponse get(@RequestParam(value = "page", required = false, defaultValue = "1L") Long page,
                                     @RequestParam(value = "size", required = false, defaultValue = "10L") Long size,
                                     @RequestParam(value = "uid") String uid) {
        singleService.page(page, size, auth.getId(), uid);
        return SystemMsgJsonResponse.success();
    }


    @GetMapping("/select")
    @ApiOperation(value = "[一对一模块] - 查询用户", httpMethod = "GET")
    public SystemMsgJsonResponse selectUser(@Valid @RequestBody SelectUserDTO dto) {
        return SystemMsgJsonResponse.success(singleService.selectUser(dto));
    }

    @PostMapping("/shield")
    @ApiOperation(value = "[一对一模块] - 屏蔽指定的用户", httpMethod = "POST")
    public SystemMsgJsonResponse shield(@RequestParam("uid") String uid) {
        singleService.shield(auth.getId(), uid);
        return SystemMsgJsonResponse.success();
    }

    @PostMapping("/remove")
    @ApiOperation(value = "[一对一模块] - 删除指定聊天记录", httpMethod = "POST")
    public SystemMsgJsonResponse remove(@RequestParam("id") Long id) {
        singleService.remove(id);
        return SystemMsgJsonResponse.success();
    }


    @PostMapping("/join")
    @ApiOperation(value = "[一对一模块] - 加入指定群聊", httpMethod = "POST")
    public SystemMsgJsonResponse join(@RequestParam("id") @NotNull Long id) {
        singleService.join(auth.getId(), id);
        return SystemMsgJsonResponse.success();
    }
}
