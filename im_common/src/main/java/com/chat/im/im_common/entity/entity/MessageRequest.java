package com.chat.im.im_common.entity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
/**
 * @author: chovychan in 2022/4/11
 */
@Setter
@Getter
@NoArgsConstructor
public class MessageRequest implements Serializable {

    private Long unionId;

    private Integer current = 1;

    private Integer size = 10;
}