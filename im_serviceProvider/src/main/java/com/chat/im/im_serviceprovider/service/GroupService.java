package com.chat.im.im_serviceprovider.service;

import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;

import java.util.List;

/**
 * @author: chovychan in 2022/5/11
 */
public interface GroupService {
    /**
     * 根据群组id获取所有成员信息
     * @param groupId 群组id
     * @return 成员信息
     */
    List<UserInfoVO> groupMember(Long groupId);
}
