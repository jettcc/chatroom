package com.chat.im.im_common.entity.entity;

import com.alibaba.fastjson.JSON;
import com.chat.im.im_common.entity.inter.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * 通信协议的消息体
 */
@Getter
@Setter
public class Invocation {

    /**
     * 类型
     */
    private String type;
    /**
     * 消息，JSON 格式
     */
    private String message;

    // 空构造方法
    public Invocation() {
    }

    public Invocation(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }
    
}