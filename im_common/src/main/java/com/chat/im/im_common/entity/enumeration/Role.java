package com.chat.im.im_common.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
/**
 * @author: chovychan in 2022/5/11
 */

@Getter
public enum Role {
    NORMAL("普通"),
    ADMIN("管理员"),
    MASTER("群主");

    @EnumValue
    public String name;

    Role(String name) {
        this.name = name;
    }
}
