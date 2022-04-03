package com.chat.im.im_oauth2server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author: AubreyChen in 2022/3/5
 */
@ApiModel("接收登录信息DTO")
@Getter
@Setter
@NoArgsConstructor
public class GetUserTokenDTO {
    @NotNull(message = "code不能为空")
    @ApiModelProperty(value = "微信code", required = true)
    String wx_code;
}
