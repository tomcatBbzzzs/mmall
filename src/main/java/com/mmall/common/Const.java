package com.mmall.common;

/**
 * @author 唐孟廷
 * @desc 常量类
 * @date 2020/5/3 - 3:37
 */
public class Const {
    /**
     * SESSION中用户的key
     */
    public static final String CURRENT_USER = "CURRENT_USER";

    /**
     * 邮箱
     */
    public static final String EMAIL = "email";

    /**
     * 用户名
     */
    public static final String USERNAME = "username";

    /**
     * 角色常量
     */
    public interface Role {
        // 普通用户
        int ROLE_CUSTOMER = 0;

        // 管理员
        int ROLE_ADMIN = 1;
    }
}
