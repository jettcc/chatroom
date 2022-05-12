package com.chat.im.im_serviceprovider.vo.message;

import com.chat.im.im_common.entity.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @author: chovychan in 2022/5/12
 */
@ApiModel("消息实体")
@Getter
@Setter
public class GroupMessageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("发送方")
    private String fromId;

    @ApiModelProperty("接收方")
    private Long toId;

    @ApiModelProperty("发送时间")
    private String createTime;

    @ApiModelProperty("内容")
    private String msgContext;

    public GroupMessageVO(Message message) {
        Optional.ofNullable(message).ifPresent(m -> {
            BeanUtils.copyProperties(m, this);
            this.createTime = m.getCreateTime().toString();
            this.toId = Long.valueOf(m.getToId());
        });
    }
}
