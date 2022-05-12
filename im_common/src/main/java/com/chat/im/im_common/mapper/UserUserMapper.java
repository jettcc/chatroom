package com.chat.im.im_common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.im.im_common.entity.dto.UserUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
/**
 * @author: chovychan in 2022/5/12
 */
@Mapper
@Repository
public interface UserUserMapper extends BaseMapper<UserUser> {
}
