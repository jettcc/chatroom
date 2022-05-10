package com.chat.im.im_common.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chat.im.im_common.entity.base.BaseAutoEntity;
import com.chat.im.im_common.entity.enumeration.MsgEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@TableName(value = "im_message",resultMap = "messageResultMap")
public class Message extends BaseAutoEntity {
    @Serial
    private static final long serialVersionUID = 3611169682695799175L;

    @ApiModelProperty("发送用户id")
    private String fromId;

    @ApiModelProperty("接受用户id")
    private String toId;

    @ApiModelProperty("内容")
    private String msgContext;

    @ApiModelProperty("签收状态")
    private Boolean haveRead;

    @ApiModelProperty("消息类型")
    private MsgEnum msgType;
}
    