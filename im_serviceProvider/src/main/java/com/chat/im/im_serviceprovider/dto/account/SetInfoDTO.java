package com.chat.im.im_serviceprovider.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: chovychan in 2022/5/9
 */
@ApiModel("修改信息DTO")
@Getter
@Setter
public class SetInfoDTO {
    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickName;
}
