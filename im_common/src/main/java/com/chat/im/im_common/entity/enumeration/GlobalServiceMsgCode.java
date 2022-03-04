package com.chat.im.im_common.entity.enumeration;

import lombok.Getter;

/**
 * <p>
 * 规定:
 * #200表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 * <p>
 * 状态码的定义
 * </p>
 *
 * @author AubreyChen in 2022/3/3
 */
@Getter
public enum GlobalServiceMsgCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_NO_PERMISSION(403, "用户无权限"),
    USER_NO_PHONE_CODE(500, "验证码错误"),


    /* 定义返回信息 */
    SERVICE_SUCCESS(0, "操作成功"),
    SERVICE_FAIL(1111, "操作失败"),

    /* 业务错误 */
    NO_PERMISSION(3001, "没有权限"),

    /* 异常 */
    ERROR_FAIL(9999, "异常"),
    TIME_OUT(-1, "请求超时"),

    /* 小程序错误 */
    WX_USER_CODE_ERROR(40029, "code无效"),
    WX_HIGH_RISK_USER(40226, "高风险用户"),
    WX_MAXIMUM_NUMBER_OF_REQUEST(45011, "该code访问次数达到上限");


    private Integer code;
    private String message;

    GlobalServiceMsgCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * 根据code获取message
     */
    public static String GlobalServiceMsgCode(Integer code) {
        for (GlobalServiceMsgCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
