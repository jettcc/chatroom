package com.chat.im.im_chatserver.controller;

import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat/msg")
@RestController
public class MsgController {
    @PostMapping("/send")
    public SystemMsgJsonResponse send() {

        return SystemMsgJsonResponse.success();
    }
}
