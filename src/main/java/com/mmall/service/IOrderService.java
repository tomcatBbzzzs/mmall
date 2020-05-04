package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @author 唐孟廷
 * @desc 订单接口
 * @date 2020/5/4 - 7:52
 */
public interface IOrderService {

    /**
     * 支付指定的订单
     *
     * @param orderNo 订单号
     * @param userId  用户id
     * @param path    二维码路径
     * @return 用户选择支付指定订单号的信息
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);


    /**
     * 支付宝回调接口
     *
     * @param params
     * @return 用于支付宝回调时使用
     */
    ServerResponse aliCallback(Map<String, String> params);


    /**
     * 查询订单支付状态
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return 返回用户支付订单后, 订单的状态
     */
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);


    /**
     * 创建订单
     *
     * @param userId     用户id
     * @param shippingId 收货地址
     * @return 创建订单, 并且指定用户的收货地址
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);


    /**
     * 取消订单
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return 取消订单
     */
    ServerResponse<String> cancel(Integer userId, Long orderNo);


    /**
     * 获取用户目前的购物车中的产品
     *
     * @param userId 用户id
     * @return 返回购物车中的产品的明细信息
     */
    ServerResponse getOrderCartProduct(Integer userId);


    /**
     * 获取订单的明细信息
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return 获取指定订单的明细信息
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);


    /**
     * 获取订单列表
     *
     * @param userId   用户id
     * @param pageNum
     * @param pageSize
     * @return 获取订单的所有信息
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


    /*---------------------
     *------ 后台服务 ------
     *---------------------*/


    /**
     * 获取所有订单然后分页
     *
     * @param pageNum
     * @param pageSize
     * @return 返回所有订单信息
     */
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);


    /**
     * 获取订单的详情信息
     *
     * @param orderNo 订单号
     * @return 返回订单的详细信息
     */
    ServerResponse<OrderVo> manageDetail(Long orderNo);


    /**
     * 模糊搜索订单信息
     *
     * @param orderNo 订单号
     * @param pageNum
     * @param pageSize
     * @return  模糊搜索订单信息
     */
    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);


    /**
     * 查看当前订单是否已经成功发货
     *
     * @param orderNo 订单号
     * @return 返回订单是否已经成功发货
     */
    ServerResponse<String> manageSendGoods(Long orderNo);


}
