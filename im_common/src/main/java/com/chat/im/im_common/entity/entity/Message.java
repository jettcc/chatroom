package com.chat.im.im_common.entity.entity;

import com.chat.im.im_common.entity.base.BaseAutoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
/**
 * @author: chovychan in 2022/5/8
 */
@ApiModel("消息实体")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Message extends BaseAutoEntity {
    @Serial
    private static final long serialVersionUID = 3611169682695799175L;

    @ApiModelProperty("发送用户id")
    private String senderId;

    @ApiModelProperty("接受用户id")
    private String receiverId;

    @ApiModelProperty("内容")
    private String msg;

    @ApiModelProperty("消息id")
    private String msgId;
}
    