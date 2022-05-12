package com.chat.im.im_serviceprovider.vo.single;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chat.im.im_common.entity.entity.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @author: chovychan in 2022/5/12
 */
@ApiModel("查询群组VO")
@Getter
@Setter
public class SelectGroupVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("群聊名称")
    private String name;

    @ApiModelProperty("群主id")
    private String masterId;

    @ApiModelProperty("静音")
    private Boolean mute;

    @ApiModelProperty("头像")
    private String avatar;

    public SelectGroupVO(Group group) {
        Optional.ofNullable(group).ifPresent(g -> {
            BeanUtils.copyProperties(g, this);
            this.createTime = g.getCreateTime().toString();
        });
    }
}
