package com.action.headline.common;
/**
 * 统一返回结果状态信息类
 *
 */
public enum ResultCodeEnum {

    SUCCESS(200, "success"),

    NOT_FOUND(404, "not found"),

    PARAM_ERROR(400, "param error"),

    NOT_LOGIN(10001, "not login"),
    USERNAME_OR_PASSWORD_ERROR(10002, "name or password error"),
    USERNAME_USED(10004, "username used"),
    ;

    private final Integer code;
    private final String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
