package com.mmall.service;


import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @author 唐孟廷
 * @desc 用户的服务接口
 * @date 2020/5/3 - 1:10
 */
public interface IUserService {
    /**
     * 用户登录的API
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回登录后用户信息
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 用户注册的API
     *
     * @param user 用户对象
     * @return 返回注册状态
     */
    ServerResponse<String> register(User user);


    /**
     * 效验指定类型的字符串是否是合法的
     *
     * @param str  字符串
     * @param type 类型
     * @return 返回效验结果
     */
    ServerResponse<String> checkValid(String str, String type);


    /**
     * 获取指定用户设置的找回答案的问题
     *
     * @param username 用户名称
     * @return 返回对应的问题
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 检查用户填写的答案是否能对应密码
     *
     * @param username 用户
     * @param question 问题
     * @param answer   答案
     * @return 返回对应的结果
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);


    /**
     * 重置密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param token       效验码
     * @return 返回密码的重置记录信息
     */
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token);


    /**
     * 登录状态下重置密码
     *
     * @param password    旧密码
     * @param passwordNew 新密码
     * @param user        用户数据
     * @return 返回密码的修改信息
     */
    ServerResponse<String> resetPassword(String password, String passwordNew, User user);


    /**
     * 修改用户信息
     *
     * @param user 用户
     * @return 返回修改的信息
     */
    ServerResponse<User> updateInfo(User user);


    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    ServerResponse<User> getInfo(Integer userId);



    /*==========================================*
     *================= 后台模块 ================*
     *==========================================*/

    /**
     * 判断当前用户是否为管理员
     *
     * @param user 用户
     * @return 当用户为管理员是返回成功
     */
    ServerResponse checkAdminRole(User user);
}
