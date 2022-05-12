package com.chat.im.im_serviceprovider.dto.single;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * @author: chovychan in 2022/5/12
 */
@ApiModel("创建群聊DTO")
@Getter
@Setter
public class CreateGroupDTO {
    @ApiModelProperty("群聊名称")
    private String name;

    @ApiModelProperty("静音")
    private Boolean mute;

    @ApiModelProperty("群描述")
    private String context;

    @ApiModelProperty("头像")
    private String avatar;
}
