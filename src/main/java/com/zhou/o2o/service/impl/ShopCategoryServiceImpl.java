package com.zhou.o2o.service.impl;

import com.zhou.o2o.dao.ShopCategoryDao;
import com.zhou.o2o.entity.ShopCategory;
import com.zhou.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺分类的业务层接口的实现类
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    /**
     * 获取店铺分类列表
     * @param shopCategoryCondition
     * @return
     */
    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(shopCategoryCondition);
    }
}
