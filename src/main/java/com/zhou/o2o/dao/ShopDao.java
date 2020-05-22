package com.zhou.o2o.dao;

import com.zhou.o2o.entity.Shop;

/**
 * 店铺持久层接口
 */
public interface ShopDao {

    /**
     * 通过shop id 查询店铺
     * @param shopId
     * @return shop
     */
    Shop queryByShopId(long shopId);


    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
