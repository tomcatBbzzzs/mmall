package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author 唐孟廷
 * @desc 分类接口
 * @date 2020/5/4 - 7:52
 */
public interface ICategoryService {
    /**
     * 添加分类
     *
     * @param categoryName 分类名称
     * @param parentId     分类的父分类id
     * @return 返回添加分类的状态信息
     */
    ServerResponse addCategory(String categoryName, Integer parentId);

    /**
     * 修改指定分类的分类名称
     *
     * @param categoryId   被修改的分类id
     * @param categoryName 被修改的分类名称
     * @return 返回修改分类的状态信息
     */
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);


    /**
     * 获取当前品类id为categoryId下的所有的子品类信息
     *
     * @param categoryId 品类id
     * @return 返回这个categoryId下的所有子品类信息
     */
    ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId);


    /**
     * 获取当前分类下的所有子分类以及子分类下的子分类
     *
     * @param categoryId 分类id
     * @return 返回当前分类下的所有子分类以及子分类下的子分类拼接出来的数据
     */
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
