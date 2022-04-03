package com.chat.im.im_oauth2server.service;

import com.alibaba.fastjson.JSONObject;
import com.chat.im.im_oauth2server.dto.GetUserTokenDTO;
import org.springframework.http.HttpHeaders;

public interface AccountService {
    /**
     * wxcode 登录接口
     * @param dto 用户提交信息
     * @return openid
     */
    String wxcodeLogin(GetUserTokenDTO dto);

    JSONObject request(String openId, HttpHeaders httpHeaders);
}
