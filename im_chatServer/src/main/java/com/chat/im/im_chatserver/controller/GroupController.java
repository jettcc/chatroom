package com.chat.im.im_chatserver.controller;

import com.chat.im.im_chatserver.component.AuthenticationInfo;
import com.chat.im.im_chatserver.service.GroupService;
import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

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
    public SystemMsgJsonResponse groupMember(@RequestParam("id") @NotNull(message = "群组id不能为空") Long groupId) {
        return SystemMsgJsonResponse.success(groupService.groupMember(groupId));
    }
}
