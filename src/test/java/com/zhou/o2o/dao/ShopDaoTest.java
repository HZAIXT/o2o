package com.zhou.o2o.dao;

import com.zhou.o2o.BaseTest;
import com.zhou.o2o.entity.Area;
import com.zhou.o2o.entity.PersonInfo;
import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * 店铺持久层测试
 */
public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    /**
     * 店铺添加测试
     */
    @Test
    @Ignore
    public void testInsertShop(){
        //店铺实体类:包含店家实体类、区域实体类、店铺类别实体类
        Shop shop = new Shop();
        //店家实体类
        PersonInfo owner = new PersonInfo();
        //区域实体类
        Area area = new Area();
        //店铺类别实体类
        ShopCategory shopCategory = new ShopCategory();

        //给用户实体类赋值
        owner.setUserId(1L);
        //给区域实体类赋值
        area.setAreaId(2);
        //给店铺类别实体类赋值
        shopCategory.setShopCategoryId(1L);
        //给shop店铺实体类赋值
        shop.setOwner(owner);//店铺所属店家信息
        shop.setArea(area);//店铺所属区域信息
        shop.setShopCategory(shopCategory);//店铺所属类别信息
        shop.setShopName("叫了只鸡");//店铺名称
        shop.setShopDesc("困了、累了，那就叫只鸡吧！");//店铺介绍
        shop.setShopAddr("广州市 天河区 天河公园 ");//店铺地址
        shop.setPhone("010-6666666");//店铺电话
        shop.setShopImg("test");//店铺门面图片地址
        shop.setCreateTime(new Date());//店铺创建时间
        shop.setEnableStatus(0);//店铺状态：-1.不可用 0.审核中 1.可用
        shop.setAdvice("审核中");

        //调用shop持久层添加店铺，返回执行sql时影响的行数
        int effectedNum = shopDao.insertShop(shop);
        //使用断言判断是否成功添加
        assertEquals(1,effectedNum);
    }

    /**
     * 店铺修改测试
     */
    @Test
    public void testUpdateShop(){
        //创建shop对象，设置想要修改的店铺id，并填充想要要修改的信息
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("国庆大促销，买一送一加饮料！");
        shop.setShopAddr("广州市 白云区  白云公园 ");
        shop.setLastEditTime(new Date());

        //调用shop持久层修改店铺，返回执行sql时影响的行数
        int effectedNum = shopDao.updateShop(shop);
        //使用断言判断是否成功添加
        assertEquals(1,effectedNum);
    }
}
