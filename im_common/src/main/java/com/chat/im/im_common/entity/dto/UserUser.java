package com.chat.im.im_common.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chat.im.im_common.entity.base.BaseAutoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel("好友关系实体")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "im_user_user")
public class UserUser extends BaseAutoEntity {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("好友id")
    private String friendId;

    @ApiModelProperty("屏蔽")
    private Boolean mute;
}
