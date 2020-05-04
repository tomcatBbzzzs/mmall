package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @author 唐孟廷
 * @desc 产品服务
 * @date 2020/5/4 - 14:03
 */
public interface IProductService {

    /**
     * 保存或更新产品, 如果产品存在则保存, 否则更新
     *
     * @param product 产品
     * @return 返回保存或更新产品后的状态信息
     */
    ServerResponse saveOrUpdateProduct(Product product);


    /**
     * 修改指定产品的状态
     *
     * @param productId 产品id
     * @param status    产品状态
     * @return 返回修改信息
     */
    ServerResponse setSaleStatus(Integer productId, Integer status);


    /**
     * 获取指定productId的产品信息
     *
     * @param productId 产品id
     * @return 返回指定产品的信息
     */
    ServerResponse manageProductDetail(Integer productId);


    /**
     * 获取分页后的产品集合
     *
     * @param pageNum  当前页
     * @param pageSize 页/数
     * @return 返回分页后的产品集合信息
     */
    ServerResponse getProductList(int pageNum, int pageSize);


    /**
     * 查询接口
     *
     * @param productName 按产品名称查询
     * @param productId   按产品id查询
     * @param pageNum
     * @param pageSize
     * @return 返回查询出来的集合
     */
    ServerResponse searchProduct(String productName, Integer productId, int pageNum, int pageSize);


    /**
     * 模糊查询,然后分页
     *
     * @param keyword    关键字
     * @param categoryId 产品id
     * @param pageNum
     * @param pageSize
     * @param orderBy    排序规则
     * @return 返回查询出来的数据并分页
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);


    /**
     * 根据产品的id获取指定的产品的信息
     *
     * @param productId 产品id
     * @return 返回指定产品的信息
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
}
