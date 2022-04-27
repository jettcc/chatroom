
package com.chat.im.im_chatclient.hearbeat;

import com.chat.im.im_common.entity.inter.MessageHandler;
import com.chat.im.im_common.hearbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class HeartbeatResponseHandler implements MessageHandler<HeartbeatResponse> {


    @Override
    public void execute(Channel channel, HeartbeatResponse message) {
        log.info("[execute][收到连接({}) 的心跳响应]", channel.id());
    }

    @Override
    public String getType() {
        return HeartbeatResponse.TYPE;
    }

}