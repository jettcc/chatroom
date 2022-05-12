package com.chat.im.im_serviceprovider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.im.im_common.entity.dto.UserGroup;
import com.chat.im.im_common.entity.dto.UserUser;
import com.chat.im.im_common.entity.entity.BaseUser;
import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.Role;
import com.chat.im.im_common.mapper.BaseUserMapper;
import com.chat.im.im_common.mapper.MessageMapper;
import com.chat.im.im_common.mapper.UserGroupMapper;
import com.chat.im.im_common.mapper.UserUserMapper;
import com.chat.im.im_serviceprovider.dto.single.SelectUserDTO;
import com.chat.im.im_serviceprovider.service.SingleService;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import com.chat.im.im_serviceprovider.vo.message.GetMessageVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author: chovychan in 2022/5/12
 */
@Service
public class SingleServiceImpl implements SingleService {
    private final BaseUserMapper baseUserMapper;
    private final UserUserMapper userUserMapper;
    private final UserGroupMapper userGroupMapper;
    private final MessageMapper messageMapper;

    public SingleServiceImpl(BaseUserMapper baseUserMapper, UserUserMapper userUserMapper, UserGroupMapper userGroupMapper, MessageMapper messageMapper) {
        this.baseUserMapper = baseUserMapper;
        this.userUserMapper = userUserMapper;
        this.userGroupMapper = userGroupMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public IPage<GetMessageVO> page(Long page, Long size, String uid, String tarId) {
        QueryWrapper<Message> qw = new QueryWrapper<>();
        qw.eq("from_id", uid);
        qw.eq("to_id", tarId);
        return new Page<GetMessageVO>(page, size)
                .setRecords(messageMapper.selectPage(new Page<>(page, size), qw)
                        .getRecords().stream().map(GetMessageVO::new).toList());
    }

    @Override
    public List<UserInfoVO> selectUser(SelectUserDTO dto) {
        QueryWrapper<BaseUser> qw = new QueryWrapper<>();
        Optional.ofNullable(dto.getId()).ifPresent(id -> qw.eq("id", id));
        Optional.ofNullable(dto.getName()).ifPresent(name -> qw.eq("name", name));
        Optional.ofNullable(dto.getNickName()).ifPresent(nickName -> qw.eq("nick_name", nickName));
        return baseUserMapper.selectList(qw).stream().map(UserInfoVO::new).toList();
    }

    @Override
    public void shield(String uid, String tarId) {
        UpdateWrapper<UserUser> uw = new UpdateWrapper<>();
        uw.eq("user_id", uid);
        uw.eq("friend_id", tarId);
        userUserMapper.update(new UserUser().setMute(true), uw);
    }

    @Override
    public void remove(Long msgId) {
        QueryWrapper<Message> qw = new QueryWrapper<>();
        qw.eq("id", msgId);
        messageMapper.delete(qw);
    }

    @Override
    public void join(String uid, Long groupId) {
        var userGroup = new UserGroup();
        userGroup.setUserId(uid)
                .setGroupId(groupId)
                .setMute(false)
                .setRole(Role.NORMAL);
        userGroupMapper.insert(userGroup);
    }

}
