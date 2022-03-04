package com.chat.im.im_common.utils;

import com.chat.im.im_common.entity.enumeration.GlobalServiceMsgCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * <p>
 *     统一数据返回,采用了LinkedHashMap的方式，不会造成多余字段
 * </p>
 * @author AubreyChen in 2022/3/3
 */
@NoArgsConstructor
public class SystemMsgJsonResponse extends LinkedHashMap<String,Object> {

    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_MESSAGE = "message";
    private static final String RESPONSE_DATA = "data";

    //标准返回
    public SystemMsgJsonResponse(int code, String msg, Object data) {
        this.put(RESPONSE_CODE, code);
        Optional.ofNullable(msg).ifPresent(s -> this.put(RESPONSE_MESSAGE, s));
        Optional.ofNullable(data).ifPresent(s -> this.put(RESPONSE_DATA, s));
    }

    public SystemMsgJsonResponse(int code, String msg) {
        this.put(RESPONSE_CODE, code);
        Optional.ofNullable(msg).ifPresent(s -> this.put(RESPONSE_MESSAGE, s));
    }

    /**
     * 成功信息返回
     */

    // 简易版成功信息返回, 只有响应码和成功信息
    public static SystemMsgJsonResponse success() {
        return new SystemMsgJsonResponse(GlobalServiceMsgCode.SUCCESS.getCode(),
                GlobalServiceMsgCode.SUCCESS.getMessage(), null);
    }

    // 成功信息返回,自定义数据
    public static SystemMsgJsonResponse success(Object data) {
        return new SystemMsgJsonResponse(GlobalServiceMsgCode.SUCCESS.getCode(),
                GlobalServiceMsgCode.SUCCESS.getMessage(), data);
    }

    /**
     * 失败信息返回
     */

    // 简易失败信息返回, 只有响应码和失败信息
    public static SystemMsgJsonResponse fail() {
        return new SystemMsgJsonResponse(GlobalServiceMsgCode.SERVICE_FAIL.getCode(), GlobalServiceMsgCode.SERVICE_FAIL.getMessage(), null);
    }
    public static SystemMsgJsonResponse fail(Object data) {
        return new SystemMsgJsonResponse(GlobalServiceMsgCode.SERVICE_FAIL.getCode(), GlobalServiceMsgCode.SERVICE_FAIL.getMessage(), data);
    }

    public static SystemMsgJsonResponse fail(GlobalServiceMsgCode code){
        return new SystemMsgJsonResponse(code.getCode(),code.getMessage(),null);
    }

    public static SystemMsgJsonResponse fail(GlobalServiceMsgCode code, Object data){
        return new SystemMsgJsonResponse(code.getCode(),code.getMessage(),data);
    }

    public static SystemMsgJsonResponse error(GlobalServiceMsgCode code,String message){
        return new SystemMsgJsonResponse(code.getCode(),message);
    }
}
