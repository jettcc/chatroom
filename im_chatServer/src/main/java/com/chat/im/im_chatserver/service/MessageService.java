package com.chat.im.im_chatserver.service;

import com.chat.im.im_chatserver.vo.message.GetUnreadMessageVO;
import com.chat.im.im_common.entity.entity.Message;

import java.util.List;
/**
 * @author: chovychan in 2022/5/11
 */
public interface MessageService {
    /**
     * 保存消息
     * @param message 消息实体
     */
    Message saveMsg(Message message);

    /**
     * 读取消息
     * @param ids 消息id数组
     */
    void readMsg(List<Long> ids);

    // List<String> pushGroupMsg(Long groupId);

    List<GetUnreadMessageVO> getUnreadMsg(String uid);
}
