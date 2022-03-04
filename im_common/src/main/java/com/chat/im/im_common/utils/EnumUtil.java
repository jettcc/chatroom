package com.chat.im.im_common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 枚举工具类, 自动映射
 * @author: AubreyChen in 2022/3/3
 */

@Component
public class EnumUtil {
    /**
     * @param clazz 枚举类型
     */
    @SneakyThrows
    public static JSONObject getEnumTypeJsonArray(Class<? extends Enum> clazz) {
        JSONObject jsonObject = new JSONObject();
        Enum[] es = clazz.getEnumConstants();
        Method getName = clazz.getMethod("getName");
        for (Enum e : es) {
            String chineseName = getName.invoke(e).toString();
            jsonObject.put(e.name(), chineseName);
        }
        return jsonObject;
    }
}
