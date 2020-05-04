package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;


/**
 * @author 唐孟廷
 * @desc 购物车接口
 * @date 2020/5/4 - 7:52
 */
public interface ICartService {

    /**
     * 添加商品到购物车
     *
     * @param userId    用户 id
     * @param productId 产品标识
     * @param count     产品数量
     * @return 添加商品到指定用户的购物车
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);


    /**
     * 更新用户的购物车
     *
     * @param userId    用户 id
     * @param productId 产品标识
     * @param count     产品数量
     * @return 修改用户购物车中的产品的数量
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);


    /**
     * 从购物车中删除产品
     *
     * @param userId     用户id
     * @param productIds 产品id
     * @return 从用户的购物车中删除指定的产品
     */
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);


    /**
     * 获取用户的购物车信息
     *
     * @param userId 用户id
     * @return 返回用户的购物车中的信息
     */
    ServerResponse<CartVo> list(Integer userId);


    /**
     * 全选/全忽略购物车
     *
     * @param userId    用户id
     * @param productId 产品id
     * @param checked   选择状态
     * @return 对用户的购物车进行操作
     */
    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);


    /**
     * 获取购物车的总价
     *
     * @param userId 用户id
     * @return 对用户的购物车中的所有产品进行总价的求和
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
