package com.chat.im.im_serviceprovider.service;

import com.chat.im.im_serviceprovider.dto.account.SetInfoDTO;
import com.chat.im.im_serviceprovider.vo.account.UserInfoVO;

public interface AccountService {
    /**
     * 修改用户信息
     * @param uid 用户id
     * @param dto 用户修改信息dto
     */
    void set(String uid, SetInfoDTO dto);

    UserInfoVO info(String uid);
}
