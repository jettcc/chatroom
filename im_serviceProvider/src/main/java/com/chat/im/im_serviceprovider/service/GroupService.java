package com.chat.im.im_serviceprovider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import com.chat.im.im_serviceprovider.vo.message.GetMessageVO;
import com.chat.im.im_serviceprovider.vo.message.GroupMessageVO;

import java.util.List;

/**
 * @author: chovychan in 2022/5/11
 */
public interface GroupService {
    /**
     * 根据群组id获取所有成员信息
     *
     * @param groupId 群组id
     * @return 成员信息
     */
    IPage<UserInfoVO> groupMember(Long page, Long size, Long groupId);

    /**
     * 查询指定群聊的聊天记录
     * @param page 分页
     * @param size 大小
     * @param id 群聊id
     * @return 聊天记录
     */
    IPage<GroupMessageVO> getGroupMsg(Long page, Long size, Long id);
}
