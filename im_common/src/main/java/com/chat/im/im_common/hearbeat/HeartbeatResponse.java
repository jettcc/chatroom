package com.chat.im.im_common.hearbeat;

import com.chat.im.im_common.entity.inter.Message;

/**
 * 消息 - 心跳响应
 * @author: chovychan in 2022/4/27
 */
public class HeartbeatResponse implements Message {

    /**
     * 类型 - 心跳响应
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String toString() {
        return "HeartbeatResponse{}";
    }

}