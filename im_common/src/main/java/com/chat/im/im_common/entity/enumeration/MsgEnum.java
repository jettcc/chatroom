package com.chat.im.im_common.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author: chovychan in 2022/5/11
 */
@Getter
public enum MsgEnum {

    CONNECT("连接", "第一次(或重连)初始化连接"),
    CHAT("聊天", "聊天消息"),
    SIGNED("读取", "消息签收"),
    KEEPALIVE("心跳", "客户端保持心跳"),
    UNREAD("获得未读消息", "获得未读消息"),
    GROUP("群组消息", "群聊消息"),
    JOIN_GROUP("加入群聊", "加入群聊"),
    NOTICE("通知", "通知: 加好友或者是进群审核, 这里的通知应该是server来发"),
    INVITE("邀请", "邀请加入群聊或者邀请成为好友"),
    AUDIT("审核", "审核加好友或者审核入群"),
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