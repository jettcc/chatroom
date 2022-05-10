package com.chat.im.im_chatserver.vo.account;

import com.chat.im.im_common.entity.entity.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @author: chovychan in 2022/5/9
 */

@ApiModel("用户信息VO")
@Getter
@Setter
public class UserInfoVO {
    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("创建时间")
    private String createTime;

    public UserInfoVO(BaseUser baseUser) {
        Optional.ofNullable(baseUser).ifPresent(user -> {
            BeanUtils.copyProperties(user, this);
            this.createTime = user.getCreateTime().toString();
        });
    }
}
