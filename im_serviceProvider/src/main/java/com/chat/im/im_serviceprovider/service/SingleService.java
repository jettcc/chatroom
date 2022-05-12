package com.chat.im.im_serviceprovider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chat.im.im_serviceprovider.dto.single.CreateGroupDTO;
import com.chat.im.im_serviceprovider.dto.single.SelectGroupDTO;
import com.chat.im.im_serviceprovider.dto.single.SelectUserDTO;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import com.chat.im.im_serviceprovider.vo.message.GetMessageVO;
import com.chat.im.im_serviceprovider.vo.single.GetGroupVO;
import com.chat.im.im_serviceprovider.vo.single.SelectGroupVO;

import java.util.List;

/**
 * @author: chovychan in 2022/5/12
 */
public interface SingleService {
    /**
     * 查询指定用户的消息记录
     *
     * @param page  分页
     * @param size  大小
     * @param uid   当前用户
     * @param tarId 指定用户
     */
    IPage<GetMessageVO> page(Long page, Long size, String uid, String tarId);

    /**
     * 查询指定用户信息
     *
     * @param dto 查询条件
     * @return 用户列表
     */
    List<UserInfoVO> selectUser(SelectUserDTO dto);

    /**
     * 添加好友
     * @param uid 用户id
     * @param tarId 目标id
     */
    void friend(String uid, String tarId);
    /**
     * 屏蔽指定用户
     *
     * @param uid   用户id
     * @param tarId 目标id
     */
    void shield(String uid, String tarId);

    /**
     * 删除指定消息
     *
     * @param msgId 消息id
     */
    void remove(Long msgId);

    /**
     * 查询指定群聊
     *
     * @param dto 查询条件
     * @return 群聊
     */
    SelectGroupVO selectGroup(SelectGroupDTO dto);

    /**
     * 加入指定群聊
     *
     * @param uid     用户id
     * @param groupId 群聊id
     */
    void join(String uid, Long groupId);

    /**
     * 查询个人加入的所有群聊
     *
     * @param uid 用户id
     * @return
     */
    List<GetGroupVO> getGroup(String uid);

    /**
     * 创建群聊
     * @param dto 创建群聊信息
     */
    void createGroup(String uid, CreateGroupDTO dto);

    /**
     * 删除群聊
     * @param id 群聊id
     */
    void delGroup(Long id);
}
