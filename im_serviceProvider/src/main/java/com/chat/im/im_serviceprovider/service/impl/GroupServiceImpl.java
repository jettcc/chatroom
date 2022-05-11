package com.chat.im.im_serviceprovider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.im.im_common.entity.dto.UserGroup;
import com.chat.im.im_common.mapper.BaseUserMapper;
import com.chat.im.im_common.mapper.UserGroupMapper;
import com.chat.im.im_serviceprovider.service.GroupService;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chovychan in 2022/5/11
 */
@Service
public class GroupServiceImpl implements GroupService {
    private final UserGroupMapper userGroupMapper;
    private final BaseUserMapper baseUserMapper;

    public GroupServiceImpl(UserGroupMapper userGroupMapper, BaseUserMapper baseUserMapper) {
        this.userGroupMapper = userGroupMapper;
        this.baseUserMapper = baseUserMapper;
    }

    @Override
    public List<UserInfoVO> groupMember(Long groupId) {
        QueryWrapper<UserGroup> qw = new QueryWrapper<>();
        qw.eq("group_id", groupId);
        return userGroupMapper.selectList(qw).stream().map(UserGroup::getUserId)
                .map(baseUserMapper::selectById).map(UserInfoVO::new).toList();
    }
}
