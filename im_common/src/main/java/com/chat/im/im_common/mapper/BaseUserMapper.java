package com.chat.im.im_common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.im.im_common.entity.entity.BaseUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author: AubreyChen in 2022/3/5
 */
@Repository
@Mapper
public interface BaseUserMapper extends BaseMapper<BaseUser> {

}
