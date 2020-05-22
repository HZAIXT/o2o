package com.zhou.o2o.service;

import com.zhou.o2o.BaseTest;
import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.Area;
import com.zhou.o2o.entity.PersonInfo;
import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.entity.ShopCategory;
import com.zhou.o2o.enums.ShopSateEnum;
import com.zhou.o2o.exceptions.ShopOperationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private  ShopService shopService;

    @Test
    public void testModifyShop() throws ShopOperationException,FileNotFoundException{
        Shop shop = new Shop();
        shop.setShopId(12L);
        shop.setShopName("修改后的店铺名称");
        //准备 InputStream对象
        File shopImg = new File("B:/aaa.jpg");
        InputStream is =new FileInputStream(shopImg);
        //更新店铺信息，包括对图片的处理
        ShopExecution shopExecution = shopService.modifyShop(shop,is,"aaa.jpg");
        System.out.println("新的图片地址为: " + shopExecution.getShop().getShopImg());
    }


    @Test
    public void testAddShop() throws ShopOperationException,FileNotFoundException {
        //准备添加店铺所需要准备的两个对象:Shop、File

        //准备Shop店铺对象
        Shop shop = new Shop();
        //准备用户对象
        PersonInfo owner = new PersonInfo();
        //准备区域对象
        Area area = new Area();
        //准备店铺分类对象
        ShopCategory shopCategory = new ShopCategory();

        //填充各个对象的数据
        owner.setUserId(1L);//设置用户id
        area.setAreaId(2);//区域id
        shopCategory.setShopCategoryId(1L);//设置分类id
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺4");//店铺名称
        shop.setShopDesc("test4");//店铺描述
        shop.setShopAddr("test4");//店铺地址
        shop.setPhone("test4");//店铺联系方式
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopSateEnum.CHECK.getState());//店铺可用状态
        shop.setAdvice("审核中");//超级管理员给店铺的提醒

        //准备 InputStream对象
        File shopImg = new File("B:/aaa.jpg");
        InputStream is =new FileInputStream(shopImg);

        //注册店铺
        ShopExecution shopExecution = shopService.addShop(shop, is,shopImg.getName());
        assertEquals(ShopSateEnum.CHECK.getState(), shopExecution.getState());
    }
}
