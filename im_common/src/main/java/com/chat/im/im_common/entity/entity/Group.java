package com.chat.im.im_common.entity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chat.im.im_common.entity.base.BaseAutoEntity;
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
@TableName(value = "im_group")
public class Group extends BaseAutoEntity {
    @ApiModelProperty("群聊名称")
    @TableField(value = "`name`")
    private String name;

    @ApiModelProperty("群主id")
    private String masterId;

    @ApiModelProperty("静音")
    private Boolean mute;

    @ApiModelProperty("头像")
    private String avatar;
}
