package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * @author 唐孟廷
 * @desc 收货地址接口
 * @date 2020/5/4 - 7:52
 */
public interface IShippingService {
    /**
     * 添加收货地址接口
     *
     * @param userId   用户id
     * @param shipping 收货地址信息
     * @return 为用户添加收货地址
     */
    ServerResponse add(Integer userId, Shipping shipping);


    /**
     * 删除收货地址
     *
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @return 删除用户指定的收货地址
     */
    ServerResponse<String> del(Integer userId, Integer shippingId);


    /**
     * 更新收货地址
     *
     * @param userId   用户id
     * @param shipping 收货地址
     * @return 修改用户的收货地址信息
     */
    ServerResponse update(Integer userId, Shipping shipping);


    /**
     * 查询用户的收货地址的详细信息
     *
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @return 返回用户的收货地址的详细信息
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);


    /**
     * 获取用户的所有收货地址
     *
     * @param userId    用户id
     * @param pageNum
     * @param pageSize
     * @return  获取用户的所有收货地址,然后分页显示
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
