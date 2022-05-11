package com.chat.im.im_common.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chat.im.im_common.entity.base.BaseAutoEntity;
import com.chat.im.im_common.entity.enumeration.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author: chovychan in 2022/5/11
 */
@ApiModel("群组实体")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "im_user_group")
public class UserGroup extends BaseAutoEntity {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("群组id")
    private String groupId;

    @ApiModelProperty("静音")
    private Boolean mute;

    @ApiModelProperty("身份")
    private Role role;
}
