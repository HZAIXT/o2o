package com.zhou.o2o.service;

import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.Shop;

import java.io.File;
import java.io.InputStream;

/**
 * 店铺服务层接口
 */
public interface ShopService {

    /**
     * 店铺的注册接口
     * @param shop  店铺对象
     * @param shopImgInputStream 店铺图片输入流
     * @param fileName 文件名字
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName);
}
