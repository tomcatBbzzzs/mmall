package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 唐孟廷
 * @desc 分类后台管理接口
 * @date 2020/5/4 - 7:34
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加品类
     *
     * @param session      会话对象
     * @param categoryName 品类名称
     * @param parentId     所属父品类id
     * @return 返回添加品类后的返回信息
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        // 1.效验当前会话是否存在用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        // 2.效验当前用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            // 增加我们处理分类的逻辑
            return iCategoryService.addCategory(categoryName, parentId);
        }

        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");

    }


    /**
     * 修改指定品类的名称
     *
     * @param session      会话对象
     * @param categoryId   品类id
     * @param categoryName 修改后的品类名称
     * @return 返回修改后的状态信息
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        // 1.效验当前会话是否存在用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        // 2.效验当前用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员, 增加我们处理分类的逻辑
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }

        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");

    }


    /**
     * 获取指定品类id下的所有品类信息
     *
     * @param session    会话对象
     * @param categoryId 品类id
     * @return 返回这个品类下的所有品类信息
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // 1.效验当前会话是否存在用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        // 2.效验当前用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员, 增加我们处理分类的逻辑
            return iCategoryService.getChildParallelCategory(categoryId);
        }

        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }


    /**
     * 获取当前分类下的所有子分类,包括子分类下的子分类
     *
     * @param session    会话对象
     * @param categoryId 分类id
     * @return 返回指定的categoryId下的所有子Category, 包括子Category下的Category
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // 1.效验当前会话是否存在用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        // 2.效验当前用户是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员, 增加我们处理分类的逻辑
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }

        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

}
