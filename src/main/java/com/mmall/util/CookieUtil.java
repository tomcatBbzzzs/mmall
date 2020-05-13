package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 唐孟廷
 * @desc 基于Cookie封装的一套工具类
 * @date 2020/5/13 - 0:12
 */
@Slf4j
public class CookieUtil {
    /**
     * COOKIE的作用域名,我们这个COOKIE将会作用在所有的*.tomcatBbzzzs.cn下
     * 在
     * www.tomcatBbzzzs.cn
     * www.a.tomcatBbzzzs.cn
     * 下都能生效
     */
    private static final String COOKIE_DOMAIN = ".tomcatBbzzzs.cn";

    /**
     * COOKIE的名称
     */
    private static final String COOKIE_NAME = "mmall_login_token";


    /**
     * 写入Cookie到请求机
     *
     * @param response response对象
     * @param token    登录信息的cookie值
     */
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);

        // 只有在指定的目录下,才能获取到cookie,我们把它放到/目录下
        // 那么它只要在tomcatBbzzzs.cn下都可以获取到cookie了
        // 比如:
        //      www.tomcatBbzzzs.cn/user/get_user_info.do   -> /user/
        //      tomcatBbzzzs.cn/user/login.do               -> /user/
        //      tomcatBbzzzs.cn/order/list.do               -> /order/
        cookie.setPath("/");

        // 不允许通过脚本来获取cookie
        cookie.setHttpOnly(true);

        // 设置cookie有效期,单位是s, 如果有效期是-1,表示永久生效
        // 如果这个maxAge不设置,则cookie不会写入硬盘,而是存放到内存。
        // 并且仅在当前页面生效, 这里我们设置为10天有效
        cookie.setMaxAge(3600 * 24 * 10);

        response.addCookie(cookie);
    }


    /**
     * 读取cookie
     *
     * @param request request对象
     * @return 返回cookie中cookieName对应的值
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("读取到cookie name:{} value:{}", cookie.getName(), cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("返回我们需要的cookie的值:{}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        return null;
    }


    /**
     * 删除cookie
     *
     * @param request  request对象
     * @param response response对象
     */
    public static void deleteLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    // 设置cookie的时间为0,代表删除此cookie
                    cookie.setMaxAge(0);
                    log.info("删除cookie name:{} value:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                }
            }
        }
    }

}
