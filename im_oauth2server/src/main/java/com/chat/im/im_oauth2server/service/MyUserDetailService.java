package com.chat.im.im_oauth2server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.im.im_common.entity.entity.BaseUser;
import com.chat.im.im_common.mapper.BaseUserMapper;
import com.chat.im.im_oauth2server.util.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p>
 * 自定义UserDetailService
 * </p>
 *
 * @author Keyvonchen in 2021/9/14
 */

@Service("userDetailService")
@RequiredArgsConstructor
@Log4j2
public class MyUserDetailService implements UserDetailsService {

    private final BaseUserMapper userMapper;

    @Value("${openId-password}")
    private String password;

    private BaseUser findUser(String openId) {
        QueryWrapper<BaseUser> qw = new QueryWrapper<>();
        qw.eq("openid", openId);
        return Optional.ofNullable(userMapper.selectOne(qw)).orElseGet(() -> {
            BaseUser user = new BaseUser();
            log.info("<< Oauth2Service.MyUserDetailService::findUser >> info: 生成新的user记录, id:[{}]", user.getId());
            user.setOpenId(openId);
            userMapper.insert(user);
            return user;
        });
    }

    @Override
    public UserDetails loadUserByUsername(String openid) throws UsernameNotFoundException {
        log.info("<< Oauth2Service.MyUserDetailService::loadUserByUsername >> check openid: [{}]", openid);
        BaseUser member = findUser(openid);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ALL"));
        String spw;
        try {
            spw = new BCryptPasswordEncoder().encode(DigestUtil.encrypt(password));
        } catch (Exception e) {
            log.error("<< Oauth2Service.MyUserDetailService::encrypt error >> error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return new User(member.getId(), spw, grantedAuthorities);
    }

    public boolean isNum(String str) {
        return Pattern.matches("^[0-9]+$", str);
    }
}
