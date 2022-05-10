package com.chat.im.im_chatserver.controller;

import com.chat.im.im_chatserver.component.AuthenticationInfo;
import com.chat.im.im_chatserver.service.MessageService;
import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: chovychan in 2022/5/10
 */
@Api(tags = "api - 消息模块")
@RequestMapping("/chat/msg")
@RestController
public class MsgController {
    private final AuthenticationInfo auth;
    private final MessageService messageService;

    public MsgController(AuthenticationInfo auth, MessageService messageService) {
        this.auth = auth;
        this.messageService = messageService;
    }

    @PostMapping("/unread")
    public SystemMsgJsonResponse send() {

        return SystemMsgJsonResponse.success();
    }
}
