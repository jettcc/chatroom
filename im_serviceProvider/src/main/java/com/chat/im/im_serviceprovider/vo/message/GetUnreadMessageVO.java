package com.chat.im.im_serviceprovider.vo.message;

import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @author: chovychan in 2022/5/10
 */
@ApiModel("获取未读消息VO")
@Getter
@Setter
public class GetUnreadMessageVO {
    @ApiModelProperty("发送时间")
    private String createTime;

    @ApiModelProperty("内容")
    private String msgContext;

    @ApiModelProperty("签收状态")
    private Boolean haveRead;

    @ApiModelProperty("消息类型")
    private MsgEnum msgType;

    public GetUnreadMessageVO(Message message) {
        Optional.ofNullable(message).ifPresent(m -> {
            BeanUtils.copyProperties(m, this);
            this.createTime = m.getCreateTime().toString();
        });

    }
}
