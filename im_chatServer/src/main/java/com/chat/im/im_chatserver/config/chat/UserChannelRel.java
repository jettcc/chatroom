package com.chat.im.im_chatserver.config.chat;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;

/**
 * 用户id和channel的关联关系处理
 *
 * @author: chovychan in 2022/5/9
 */
@Log4j2
public class UserChannelRel {

    private static final HashMap<String, Channel> manager = new HashMap<>();

    public static void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            log.info("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}