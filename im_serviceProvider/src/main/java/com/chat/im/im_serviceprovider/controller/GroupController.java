package com.chat.im.im_serviceprovider.controller;

import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import com.chat.im.im_serviceprovider.component.AuthenticationInfo;
import com.chat.im.im_serviceprovider.service.GroupService;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

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
    @ApiOperation(value = "查询指定群组的成员", httpMethod = "GET", response = UserInfoVO.class)
    public SystemMsgJsonResponse groupMember(@RequestParam("id") @NotNull(message = "群组id不能为空") Long groupId) {
        return SystemMsgJsonResponse.success(groupService.groupMember(groupId));
    }


}
