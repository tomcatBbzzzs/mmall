package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 唐孟廷
 * @desc 用户的controller类
 * @date 2020/5/3 - 1:00
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;


    /**
     * 用户登录接口
     *
     * @param username 用户名称
     * @param password 用户密码
     * @param session  会话对象
     * @return 返回用户登录信息
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        ServerResponse<User> res = iUserService.login(username, password);
        // cookie中存储的JSESSIONID=C6DCBF7F62B6D9FE7ECBDDEF47F80DB9

        if (res.isSuccess()) {
            log.info("保存用户信息:\n" + res.getData());
            // 将sessionId写入浏览器的cookie
            CookieUtil.writeLoginToken(response, session.getId());

            // 将登录信息存入到Redis
            RedisPoolUtil.setEx(session.getId(), JsonUtil.beanToString(res.getData()), Const.Redis.ONE_HOUR);
        }
        return res;
    }


    /**
     * 用户退出接口
     *
     * @param session 会话对象
     * @return 返回用户退出状态
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//        session.removeAttribute(Const.CURRENT_USER);

        // 取出本地的cookile
        String sessionId = CookieUtil.readLoginToken(request);

        // 让存放在Redis中的sessionId失效
        RedisPoolUtil.expire(sessionId, 0);

        // 删除本地的cookile
        CookieUtil.deleteLoginToken(request, response);


        return ServerResponse.createBySuccess();
    }


    /**
     * 用户注册接口
     *
     * @param user 用户信息
     * @return 返回用户注册状态
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestBody User user) {
        return iUserService.register(user);
    }


    /**
     * 效验指定类型的值是否是可以使用的
     *
     * @param str  值
     * @param type 类型
     * @return 返回是否可以被使用
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }


    /**
     * 获取当前登录的用户的信息
     *
     * @param session 会话
     * @return 返回会话中的用户信息
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request, HttpSession session) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);

        // 现在我们直接从cookie中获取存储的 mmall_login_token 对应的sessionId
        String sessionId = CookieUtil.readLoginToken(request);

        // 如果我们在cookie中找不到sessionId
        if (StringUtils.isEmpty(sessionId)) {
            return ServerResponse.createByErrorMessage("当前用户未登录");
        } else {

            // 然后从redis-server中取出sessionId对应的User序列化后的信息
            User user = JsonUtil.stringToBean(RedisPoolUtil.get(sessionId), User.class);

            log.info("当前用户登录信息:\n" + user);

            if (user == null) {
                return ServerResponse.createByErrorMessage("当前用户未登录");
            }
            return ServerResponse.createBySuccess(user);
        }
    }


    /**
     * 获取指定用户设置的找回答案的问题
     *
     * @param username 用户名
     * @return 返回用户设置的问题
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }


    /**
     * 检查用户设置的问题是否能和答案想对应
     *
     * @param username 用户名称
     * @param question 问题
     * @param answer   答案
     * @return 返回验证后的token
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }


    /**
     * 通过回答问题来修改密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param token       token数据
     * @return 返回修改密码是否成功
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token) {
        return iUserService.forgetResetPassword(username, passwordNew, token);
    }


    /**
     * 登录状态下更新用户密码
     *
     * @param session     会话对象
     * @param password    旧密码
     * @param passwordNew 新密码
     * @return 返回更新密码的操作状态
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String password, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(password, passwordNew, user);
    }


    /**
     * 修改用户的信息
     *
     * @param session 会话对象
     * @param user    用户信息
     * @return 修改session中的用户信息
     */
    @RequestMapping(value = "update_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_info(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());

        ServerResponse<User> response = iUserService.updateInfo(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }


    @RequestMapping(value = "get_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> get_info(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,即将跳转登录");
        }
        return iUserService.getInfo(currentUser.getId());
    }


}
