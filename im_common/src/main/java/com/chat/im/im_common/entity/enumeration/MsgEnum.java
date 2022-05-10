package com.chat.im.im_common.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MsgEnum {

    CONNECT("链接消息", "第一次(或重连)初始化连接"),
    CHAT("聊天消息", "聊天消息"),
    SIGNED("读取消息", "消息签收"),
    KEEPALIVE("心跳消息", "客户端保持心跳"),
    PULL_FRIEND("操作消息", "拉取好友");

    @EnumValue
    public final String type;

    public final String name;

    MsgEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }
}