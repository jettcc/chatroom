package com.chat.im.im_chatclient.hearbeat;

import com.chat.im.im_common.entity.entity.Invocation;
import com.chat.im.im_common.entity.inter.MessageHandler;
import com.chat.im.im_common.hearbeat.HeartbeatRequest;
import com.chat.im.im_common.hearbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
/**
 * 客户端心跳检测
 * @author: chovychan in 2022/4/27
 */
@Component
@Log4j2
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {
    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[客户端] 收到连接 {} 的心跳...", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }

}