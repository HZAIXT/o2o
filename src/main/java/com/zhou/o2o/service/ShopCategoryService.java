package com.zhou.o2o.service;

import com.zhou.o2o.entity.ShopCategory;

import java.util.List;

/**
 * 店铺分类的业务层接口
 */
public interface ShopCategoryService {

    /**
     * 获取店铺分类列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}





















