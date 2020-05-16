package com.zhou.o2o.service;

import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.Shop;

import java.io.File;

/**
 * 店铺服务层接口
 */
public interface ShopService {

    /**
     * 店铺的注册接口
     * @param shop  店铺对象
     * @param shopImg 店铺图片
     * @return
     */
    ShopExecution addShop(Shop shop, File shopImg);
}
