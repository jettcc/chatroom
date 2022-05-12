package com.chat.im.im_serviceprovider.dto.single;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: chovychan in 2022/5/12
 */
@ApiModel("查询群组DTO")
@Getter
@Setter
public class SelectGroupDTO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("群聊名称")
    private String name;
}
