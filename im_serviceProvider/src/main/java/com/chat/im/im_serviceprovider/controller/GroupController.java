package com.chat.im.im_serviceprovider.controller;

import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import com.chat.im.im_serviceprovider.component.AuthenticationInfo;
import com.chat.im.im_serviceprovider.service.GroupService;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import com.chat.im.im_serviceprovider.vo.message.GetMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author: chovychan in 2022/5/11
 */
@Api(tags = "api - 群组模块")
@RestController
@RequestMapping("/chat/group")
public class GroupController {
    private final AuthenticationInfo auth;
    private final GroupService groupService;

    public GroupController(AuthenticationInfo auth, GroupService groupService) {
        this.auth = auth;
        this.groupService = groupService;
    }

    @GetMapping("/group-member")
    @ApiOperation(value = "[群组模块] - 查询指定群组的成员", httpMethod = "GET", response = UserInfoVO.class)
    public SystemMsgJsonResponse groupMember(@RequestParam("id") @NotNull(message = "群组id不能为空") Long id,
                                             @RequestParam(value = "page", required = false, defaultValue = "1L") Long page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10L") Long size) {
        return SystemMsgJsonResponse.success(groupService.groupMember(page, size, id));
    }

    @GetMapping("/page")
    @ApiOperation(value = "[群组模块] - 分页查询群聊的聊天记录", httpMethod = "GET", response = GetMessageVO.class)
    public SystemMsgJsonResponse getGroupMsg(@RequestParam("id") @Null(message = "群聊id不能为空") Long id,
                                             @RequestParam(value = "page", required = false, defaultValue = "1L") Long page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10L") Long size) {
        return SystemMsgJsonResponse.success(groupService.getGroupMsg(page, size, id));
    }
}
