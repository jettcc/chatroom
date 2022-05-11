package com.chat.im.im_serviceprovider.service.impl;

import com.chat.im.im_common.entity.entity.BaseUser;
import com.chat.im.im_common.mapper.BaseUserMapper;
import com.chat.im.im_serviceprovider.component.SourceMapper;
import com.chat.im.im_serviceprovider.dto.account.SetInfoDTO;
import com.chat.im.im_serviceprovider.service.AccountService;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: chovychan in 2022/5/9
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final String TAG = "AccountServiceImpl";
    private final SourceMapper sourceMapper;
    private final BaseUserMapper baseUserMapper;

    public AccountServiceImpl(SourceMapper sourceMapper, BaseUserMapper baseUserMapper) {
        this.sourceMapper = sourceMapper;
        this.baseUserMapper = baseUserMapper;
    }

    @Override
    public void set(String uid, SetInfoDTO dto) {
        BaseUser user = findUser(uid, "set");
        // 非空就更新
        Optional.ofNullable(dto.getName()).ifPresent(user::setName);
        Optional.ofNullable(dto.getAvatar()).ifPresent(user::setAvatar);
        Optional.ofNullable(dto.getNickName()).ifPresent(user::setNickName);
        baseUserMapper.updateById(user);
    }

    @Override
    public UserInfoVO info(String uid) {
        return new UserInfoVO(findUser(uid, "info"));
    }

    private BaseUser findUser(String uid, String funcName) {
        return sourceMapper.findUser(TAG, funcName, uid);
    }
}
