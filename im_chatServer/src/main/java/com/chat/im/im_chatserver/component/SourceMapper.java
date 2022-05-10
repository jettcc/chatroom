package com.chat.im.im_chatserver.component;

import com.chat.im.im_common.entity.entity.BaseUser;
import com.chat.im.im_common.mapper.BaseUserMapper;
import com.chat.im.im_common.mapper.MessageMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 整体资源管理
 * @author: chovychan in 2022/5/9
 */
@Component
public class SourceMapper {
    private final BaseUserMapper baseUserMapper;
    private final MessageMapper messageMapper;
    private final RedisMapper redisMapper;
    private static final long REDIS_TIME = 3 * 60;

    public SourceMapper(BaseUserMapper baseUserMapper, MessageMapper messageMapper, RedisMapper redisMapper) {
        this.baseUserMapper = baseUserMapper;
        this.messageMapper = messageMapper;
        this.redisMapper = redisMapper;
    }


    public BaseUser findUser(String TAG, String funcName, String uid) {
        String key = "USER_" + uid;
        if (redisMapper.hasKey(key)) return (BaseUser) redisMapper.get(key);

        return Optional.ofNullable(baseUserMapper.selectById(uid)).map(u -> {
            redisMapper.set(key, u, REDIS_TIME);
            return u;
        }).orElseThrow(() -> {
            LogMapper.error(TAG, funcName, "该id:" + uid + "查找不到指定用户, 请检查");
            return new RuntimeException();
        });
    }
}
