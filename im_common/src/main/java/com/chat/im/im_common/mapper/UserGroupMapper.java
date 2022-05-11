package com.chat.im.im_common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.im.im_common.entity.dto.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author: chovychan in 2022/5/11
 */
@Mapper
@Repository
public interface UserGroupMapper extends BaseMapper<UserGroup> {
}
