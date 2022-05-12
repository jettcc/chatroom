package com.chat.im.im_serviceprovider.vo.message;

import com.chat.im.im_common.entity.entity.Message;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
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
public class GetMessageVO {
    @ApiModelProperty("发送时间")
    private String createTime;

    @ApiModelProperty("内容")
    private String msgContext;

//    @ApiModelProperty("签收状态")
//    private Boolean haveRead;

    public GetMessageVO(Message message) {
        Optional.ofNullable(message).ifPresent(m -> {
            BeanUtils.copyProperties(m, this);
            this.createTime = m.getCreateTime().toString();
        });
    }
}
