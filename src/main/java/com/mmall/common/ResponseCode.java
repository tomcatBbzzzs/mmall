package com.mmall.common;

/**
 * @author 唐孟廷
 * @desc ServerResponse中属性枚举类
 * @date 2020/5/3 - 1:18
 */
public enum ResponseCode {
    // 成功
    SUCCESS(0,"SUCCESS"),

    // 失败
    ERROR(1,"ERROR"),

    // 未登录
    NEED_LOGIN(10,"NEED_LOGIN"),

    // 参数错误
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;


    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
