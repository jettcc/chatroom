package com.chat.im.im_oauth2server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("创建账号DTO")
@Getter
@Setter
@NoArgsConstructor
public class CreateAccountDTO {
    @NotNull(message = "微信code不能为空")
    @ApiModelProperty("微信code")
    String code;
}
