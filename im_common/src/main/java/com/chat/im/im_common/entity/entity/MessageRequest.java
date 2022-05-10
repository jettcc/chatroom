package com.chat.im.im_common.entity.entity;

import com.chat.im.im_common.entity.enumeration.MsgEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: chovychan in 2022/4/11
 */
@ApiModel("消息dto")
@Setter
@Getter
@NoArgsConstructor
public class MessageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8021381444738260454L;
    @ApiModelProperty("消息类型")
    private MsgEnum msgType;

    @ApiModelProperty("消息实体")
    private Message msg;

    @ApiModelProperty("拓展字段")
    private String extend;        // 扩展字段
}