package com.chat.im.im_common.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MsgEnum {

    CONNECT(1, "第一次(或重连)初始化连接"),
    CHAT(2, "聊天消息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "客户端保持心跳"),
    PULL_FRIEND(5, "拉取好友");

    @EnumValue
    public final Integer type;

    public final String name;

    MsgEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }
}