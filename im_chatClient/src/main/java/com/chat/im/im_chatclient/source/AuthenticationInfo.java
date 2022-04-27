package com.chat.im.im_chatclient.source;

import com.alibaba.fastjson.JSONObject;
import com.chat.im.im_chatclient.mapper.RedisMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * @author: chovychan in 2022/4/27
 */
@Component
@Log4j2
public class AuthenticationInfo {
    private final RedisMapper redisMapper;

    public AuthenticationInfo(RedisMapper redisMapper) {
        this.redisMapper = redisMapper;
    }

    public String getId() {
        String ustr = getServletRequest();
        var base64_str = EncodeStr(ustr);
        if (redisMapper.hasKey(base64_str)) {
            return (String) redisMapper.get(base64_str);
        }
        return parseRequestGetUserInfo(ustr);
    }

    private String parseRequestGetUserInfo(String ustr) {
        String val = JSONObject.parseObject(ustr)
                .getString("_id");
        // redis 存 <base64(user_id), user_id>
        redisMapper.set(EncodeStr(ustr), val, 3 * 60);
        return val;
    }

    private String EncodeStr(String ustr) {
        // return base64(ustr.hashCode)
        return Base64.getUrlEncoder()
                .encodeToString(String.valueOf(ustr.hashCode())
                .getBytes(StandardCharsets.UTF_8));
    }

    private String getServletRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).orElseThrow(() -> {
            log.error("<< ServerProvider::AuthenticationInfo::ERROR >> error info: [servlet is null]");
            throw new RuntimeException("servlet is null");
        }).getRequest().getHeader("user");
    }
}
