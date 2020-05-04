package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author 唐孟廷
 * @desc 请在这里描述类的作用
 * @date 2020/5/4 - 7:57
 */
@Service("com.mmall.service.impl.CategoryServiceImpl")
public class CategoryServiceImpl implements ICategoryService {
    private static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加分类
     *
     * @param categoryName 分类名称
     * @param parentId     分类的父分类id
     * @return 返回添加分类的状态信息
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数不能为null");
        }

        // 判断这个品类名称是否已经被使用
        int row = categoryMapper.selectCategoryByCategoryName(categoryName);
        if (row > 0) {
            return ServerResponse.createByErrorMessage("这个品类的名字已经被使用了");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);

        // 设置分类可用
        category.setStatus(true);


        row = categoryMapper.insert(category);
        if (row > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }


    /**
     * 修改指定分类的分类名称
     *
     * @param categoryId   被修改的分类id
     * @param categoryName 被修改的分类名称
     * @return 返回修改分类的状态信息
     */
    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数不能为null");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int row = categoryMapper.selectCategoryByCategoryName(categoryName);
        if (row > 0) {
            return ServerResponse.createByErrorMessage("这个品类的名字已经被使用了");
        }

        row = categoryMapper.updateByPrimaryKeySelective(category);
        if (row > 0) {
            return ServerResponse.createBySuccess("修改品类成功");
        }
        return ServerResponse.createByErrorMessage("修改品类失败");
    }


    /**
     * 获取当前品类id为categoryId下的所有的子品类信息
     *
     * @param categoryId 品类id
     * @return 返回这个categoryId下的所有子品类信息
     */
    @Override
    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId) {
        // 1.获取categoryId下的所有的Category
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);

        if (CollectionUtils.isEmpty(categoryList)) {
            log.info(String.format("分类 %d 下没有子分类", categoryId));
        }

        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 获取当前分类下的所有子分类以及子分类下的子分类
     *
     * @param categoryId 分类id
     * @return 返回当前分类下的所有子分类以及子分类下的子分类拼接出来的数据
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        List<Integer> categoryIdList = Lists.newArrayList();

        // 获取当前categoryId下的所有category信息, 然后存放到categorySet中
        findChildCategory(categoryId, categorySet);

        if(categoryId != null) {
            categorySet.forEach(category -> categoryIdList.add(category.getId()));
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }


    /**
     * 递归获取当前分类下的所有子分类的信息,然后添加到一个去重的Set中
     *
     * @param categoryId  当前分类id
     * @param categorySet 存放结果的Set
     */
    private void findChildCategory(Integer categoryId, Set<Category> categorySet) {
        // 1.获取当前 分类id 的分类信息
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null) {
            categorySet.add(category);
        }

        // 2.获取当前 分类id 下的所有子分类的信息
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);

        // 3.如果当前分类下还存在子分类才进行递归
        if (!CollectionUtils.isEmpty(categoryList)) {
            // 递归获取当前子分类下的所有子分类信息
            categoryList.forEach(c -> {
                categorySet.add(c);
                findChildCategory(c.getId(), categorySet);
            });
        }

    }


}
