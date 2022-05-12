package com.chat.im.im_serviceprovider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.im.im_common.entity.dto.UserGroup;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import com.chat.im.im_common.mapper.BaseUserMapper;
import com.chat.im.im_common.mapper.MessageMapper;
import com.chat.im.im_common.mapper.UserGroupMapper;
import com.chat.im.im_serviceprovider.service.GroupService;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import com.chat.im.im_serviceprovider.vo.message.GroupMessageVO;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chovychan in 2022/5/11
 */
@Service
public class GroupServiceImpl implements GroupService {
    private final UserGroupMapper userGroupMapper;
    private final BaseUserMapper baseUserMapper;
    private final MessageMapper messageMapper;

    public GroupServiceImpl(UserGroupMapper userGroupMapper, BaseUserMapper baseUserMapper, MessageMapper messageMapper) {
        this.userGroupMapper = userGroupMapper;
        this.baseUserMapper = baseUserMapper;
        this.messageMapper = messageMapper;
    }

    private enum Ops {
        MUTE,
        OUT;
    }

    @Override
    public IPage<UserInfoVO> groupMember(Long page, Long size, Long groupId) {
        QueryWrapper<UserGroup> qw = new QueryWrapper<>();
        qw.eq("group_id", groupId);
        return new Page<UserInfoVO>(page, size)
                .setRecords(userGroupMapper.selectList(qw)
                        .stream().map(UserGroup::getUserId)
                        .map(baseUserMapper::selectById)
                        .map(UserInfoVO::new).toList());
    }

    @Override
    public IPage<GroupMessageVO> getGroupMsg(Long page, Long size, Long id) {
        QueryWrapper<Message> qw = new QueryWrapper<>();
        qw.eq("msg_type", MsgEnum.GROUP.getType());
        qw.eq("group_id", id);
        return new Page<GroupMessageVO>()
                .setRecords(messageMapper.selectList(qw)
                        .stream().map(GroupMessageVO::new).toList());
    }

    @Override
    public void quitGroup(String uid, Long id) {
        QueryWrapper<UserGroup> qw = new QueryWrapper<>();
        qw.eq("user_id", uid);
        qw.eq("group_id", id);
        userGroupMapper.delete(qw);
    }

    @Override
    public void outMember(String type, Long gid, String id) {
        Ops ops = Ops.valueOf(type);
        QueryWrapper<UserGroup> qw = new QueryWrapper<>();
        qw.eq("group_id", gid);
        qw.eq("user_id", id);
        switch (ops) {
            case MUTE -> userGroupMapper.update(new UserGroup().setMute(true), qw);
            case OUT -> userGroupMapper.delete(qw);
        }
    }
}
