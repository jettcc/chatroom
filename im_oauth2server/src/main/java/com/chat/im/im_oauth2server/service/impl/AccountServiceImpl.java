package com.chat.im.im_oauth2server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chat.im.im_common.entity.enumeration.GlobalServiceMsgCode;
import com.chat.im.im_oauth2server.dto.GetUserTokenDTO;
import com.chat.im.im_oauth2server.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${xcx-appid}")
    private String appid;

    @Value("${xcx-secret}")
    private String secret;

    @Value("${openId-password}")
    private String password;

    private final static String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private final static String OAUTH_TOKEN_URL = "http://localhost:4300/oauth/token";

    @Override
    public String wxcodeLogin(GetUserTokenDTO dto) {
        return getOpenId(dto.getWx_code());
    }

    @Override
    public JSONObject request(String openId, HttpHeaders httpHeaders) {
        // 使用客户端的请求头,发起请求
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 强制移除原来的请求头,防止token失效
        httpHeaders.remove(HttpHeaders.AUTHORIZATION);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Basic YW5kcm9pZDphbmRyb2lk");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, httpHeaders);
        return accessHttpRequest(openId, request);
    }

    private JSONObject accessHttpRequest(String openId, HttpEntity<MultiValueMap<String, String>> request) {
        String accessTokenUrl = OAUTH_TOKEN_URL + "?grant_type=password&username=" + openId + "&password=" + password;
        return restTemplate.postForObject(accessTokenUrl, request, JSONObject.class);
    }

    private String getOpenId(String code) {
        // GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        String url = WX_LOGIN_URL + "?appid=" + appid
                + "&secret=" + secret + "&js_code=" + code + "&&grant_type=authorization_code";
        JSONObject entity = Optional.ofNullable(restTemplate.getForObject(url, JSONObject.class))
                .orElseThrow(() -> new RuntimeException("获取openId异常, 请检查code"));
        Integer errCode = entity.getInteger("errcode");
        Optional.ofNullable(errCode).ifPresent(e -> {
            String errmsg = GlobalServiceMsgCode
                    .GlobalServiceMsgCode(getOpenIdErrorCode(e)
                            .getCode());
            log.error("AccountServiceImpl::getOpenId ERROR: {}", errmsg);
            throw new RuntimeException(errmsg);
        });
        var data = entity.getInnerMap();
        assert data.get("openid") != null;
        return (String) data.get("openid");
    }

    private GlobalServiceMsgCode getOpenIdErrorCode(int errCode) {
        return GlobalServiceMsgCode.getMsgEnumByCode(errCode);
    }
}
