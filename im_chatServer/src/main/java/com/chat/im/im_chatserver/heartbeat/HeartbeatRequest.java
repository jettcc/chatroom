package com.chat.im.im_chatserver.heartbeat;

import com.chat.im.im_common.entity.inter.Message;

/**
 * 消息 - 心跳请求
 * @author: chovychan in 2022/4/27
 */
public class HeartbeatRequest implements Message {
    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRequest{}";
    }
}