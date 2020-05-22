package com.zhou.o2o.service;

import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

/**
 * 店铺服务层接口
 */
public interface ShopService {

    /**
     * 通过店铺Id获取店铺信息
     *
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);


    /**
     * 更新店铺信息，包括对图片的处理
     *
     * @param shop
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;


    /**
     * 注册店铺信息，包括图片处理
     * @param shop  店铺对象
     * @param shopImgInputStream 店铺图片输入流
     * @param fileName 文件名字
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName);
}
