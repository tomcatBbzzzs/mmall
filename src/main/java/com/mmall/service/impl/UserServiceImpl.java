package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.mmall.common.ServerResponse.createByErrorMessage;

/**
 * @author tangmengting
 * @date 2020/05/03 03:24
 */
@Service("com.mmall.service.impl.UserServiceImpl")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录的API
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回登录后用户信息
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        // 1.查看用户是否存在
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return createByErrorMessage("用户名不存在");
        }

        // 2.MD5密码加密, 比较加密后的密码是否匹配
        User user = userMapper.selectLogin(username, MD5Util.MD5EncodeUtf8(password));
        if (user == null) {
            return createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 用户注册的API
     *
     * @param user 用户对象
     * @return 返回注册状态
     */
    @Override
    public ServerResponse<String> register(User user) {
        // 1.检测用户名是否已经被注册
        ServerResponse validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        // 2.检测邮箱是否已经被注册
        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }


        // 3.设置角色分类, 以及堆密码进行MD5加密
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        // 4.保存到数据库
        int rowCount = userMapper.insert(user);

        if (rowCount == 0) {
            return createByErrorMessage("服务器繁忙...");
        }

        return ServerResponse.createBySuccess("用户注册成功");
    }

    /**
     * 效验指定类型的字符串是否是合法的
     *
     * @param str  字符串
     * @param type 类型
     * @return 返回效验结果
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(str)) {
            int resultCount = 0;

            // 1.效验用户名
            if (Const.USERNAME.equals(type)) {
                resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return createByErrorMessage("用户名已存在");
                }
            }

            // 2.效验邮箱
            if (Const.EMAIL.equals(type)) {
                resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return createByErrorMessage("email已存在");
                }
            }
        } else {
            return createByErrorMessage("参数不能为null");
        }

        return ServerResponse.createBySuccess("效验成功");
    }


    /**
     * 获取指定用户设置的找回答案的问题
     *
     * @param username 用户名称
     * @return 返回对应的问题
     */
    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);

        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return createByErrorMessage("用户没有设置找回答案的问题");
    }

    /**
     * 检查用户填写的答案是否能对应密码
     *
     * @param username 用户
     * @param question 问题
     * @param answer   答案
     * @return 返回对应的结果
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 说明问题以及答案是这个用户的,并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return createByErrorMessage("问题和答案不能匹配");
    }


    /**
     * 重置密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param token       效验码
     * @return 返回密码的重置记录信息
     */
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token) {
        if (StringUtils.isBlank(token)) {
            return createByErrorMessage("参数错误, token不能为null");
        }
        ServerResponse validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return createByErrorMessage("用户不存在");
        }

        String cacheToken = TokenCache.getKey(username);
        if (StringUtils.isBlank(cacheToken) || TokenCache.invalidValue(cacheToken)) {
            return createByErrorMessage("token无效或者已经过期");
        }

        if (!StringUtils.equals(token, cacheToken)) {
            return createByErrorMessage("服务器中的token和传入的token不一致");
        }

        int row = userMapper.updatePasswordByUsername(username, MD5Util.MD5EncodeUtf8(passwordNew));
        if (row > 0) {
            // 清空token
            TokenCache.removeKey(username);
            return ServerResponse.createBySuccess("密码修改成功");
        }

        return createByErrorMessage("密码修改失败");
    }


    /**
     * 登录状态下重置密码
     *
     * @param password    旧密码
     * @param passwordNew 新密码
     * @param user        用户数据
     * @return 返回密码的修改信息
     */
    @Override
    public ServerResponse<String> resetPassword(String password, String passwordNew, User user) {
        if (!StringUtils.isNotBlank(passwordNew)) {
            return createByErrorMessage("修改后的密码不能为null");
        }

        // 检测用户和密码是否能够对应, 防止横向越权
        int row = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(password), user.getId());
        if (row == 0) {
            return createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        // 修改用户数据
        row = userMapper.updateByPrimaryKeySelective(user);

        if (row > 0) {
            ServerResponse.createBySuccess("密码更新成功");
        }

        return createByErrorMessage("密码更新失败");
    }


    /**
     * 修改用户信息
     *
     * @param user 用户
     * @return 返回修改的信息
     */
    @Override
    public ServerResponse<User> updateInfo(User user) {
        // username是不能被更新的
        // email效验一个是不是已经存在, 并且存在的email如果相同你的话, 不能是我们当前的这个用户的

        int row = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (row > 0) {
            return createByErrorMessage("email已经存在, 请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        row = userMapper.updateByPrimaryKeySelective(updateUser);

        if (row > 0) {
            ServerResponse.createBySuccess("用户信息已更新", updateUser);
        }

        return ServerResponse.createByErrorMessage("用户信息更新失败");
    }


    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public ServerResponse<User> getInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    /**
     * 判断当前用户是否为管理员
     *
     * @param user 用户
     * @return 当用户为管理员是返回成功
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
