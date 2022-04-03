package com.chat.im.im_oauth2server.config.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.im.im_common.entity.entity.BaseUser;
import com.chat.im.im_common.mapper.BaseUserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: AubreyChen in 2022/3/9
 */
@Component
@Log4j2
public class MyJwtTokenEnhancer implements TokenEnhancer {
    private final BaseUserMapper baseUserMapper;

    public MyJwtTokenEnhancer(BaseUserMapper baseUserMapper) {
        this.baseUserMapper = baseUserMapper;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String id = authentication.getName();
        QueryWrapper<BaseUser> qw = new QueryWrapper<BaseUser>()
                .eq("id", id);
        BaseUser member = baseUserMapper.selectOne(qw);
        Optional.ofNullable(member).orElseThrow(() -> {
            log.error("<< Oauth2Server::MyJwtTokenEnhancer::enhance >> error: select user fail, id:[{}]", id);
            throw new RuntimeException("select user fail, id: [" + id + "]");
        });
        log.info("<< Oauth2Server::MyJwtTokenEnhancer::enhance >> info: select user info: [{}]", member.getId());
        Map<String, Object> info = new HashMap<>();
        info.put("_id", member.getId());
        info.put("open_id", member.getOpenId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
