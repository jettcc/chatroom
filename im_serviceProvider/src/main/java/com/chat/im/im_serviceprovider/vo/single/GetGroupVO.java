package com.chat.im.im_serviceprovider.vo.single;

import com.chat.im.im_common.entity.dto.UserGroup;
import com.chat.im.im_common.entity.entity.Group;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.util.Optional;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
/**
 * @author: chovychan in 2022/5/12
 */
@ApiModel("用户查询加入群聊列表VO")
@Getter
@Setter
public class GetGroupVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("加入时间")
    private String createTime;

    @ApiModelProperty("群聊名称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    public GetGroupVO(UserGroup group) {
        Optional.ofNullable(group).ifPresent(g -> {
            BeanUtils.copyProperties(g, this);
            this.createTime = g.getCreateTime().toString();
        });
    }
}
