package com.chat.im.im_oauth2server.config.security;


import com.chat.im.im_oauth2server.util.DigestUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 *     自定义加密策略
 * </p>
 * @author Keyvonchen in 2021/9/14
 */
public class NoEncryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return (String) charSequence;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (rawPassword == null || encodedPassword == null) {
            return false;
        } else {
            try {
                return bCryptPasswordEncoder.matches(DigestUtil
                                .encrypt((String) rawPassword),
                        encodedPassword);
            } catch (Exception e) {
                return false;
            }
        }

    }
}