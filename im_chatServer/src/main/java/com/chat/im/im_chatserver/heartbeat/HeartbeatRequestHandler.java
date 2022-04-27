package com.chat.im.im_chatserver.heartbeat;

import com.chat.im.im_common.entity.entity.Invocation;
import com.chat.im.im_common.entity.inter.MessageHandler;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
/**
 * 服务端心跳检测
 * @author: chovychan in 2022/4/27
 */
@Component
@Log4j2
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {
    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }

}