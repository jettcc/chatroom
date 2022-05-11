package com.chat.im.im_chatserver.service;

import com.chat.im.im_chatserver.vo.account.UserInfoVO;

import java.util.List;

public interface GroupService {
    /**
     * 根据群组id获取所有成员信息
     * @param groupId 群组id
     * @return 成员信息
     */
    List<UserInfoVO> groupMember(Long groupId);
}
