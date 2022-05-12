package com.chat.im.im_serviceprovider.dto.single;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: chovychan in 2022/5/12
 */
@ApiModel("查询指定用户DTO")
@Getter
@Setter
public class SelectUserDTO {
    @ApiModelProperty("用户id")
    String id;

    @ApiModelProperty("用户名称")
    String name;

    @ApiModelProperty("用户昵称")
    String nickName;
}
