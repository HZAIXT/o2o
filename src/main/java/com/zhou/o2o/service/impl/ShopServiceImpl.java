package com.zhou.o2o.service.impl;

import com.zhou.o2o.dao.ShopDao;
import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.enums.ShopSateEnum;
import com.zhou.o2o.exceptions.ShopOperationException;
import com.zhou.o2o.service.ShopService;
import com.zhou.o2o.util.ImageUtil;
import com.zhou.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 店铺服务层接口实现类
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;


    /**
     * 店铺的注册方法
     * @param shop  店铺对象
     * @param shopImg 店铺图片
     * @return 店铺的数据传输类当前状态
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, File shopImg) {
        /**
         * 店铺注册步骤:
         *  1.将店铺的信息插入到数据库中并返回店铺的id
         *  2.根据店铺的id去创建出存储图片的文件夹并处理存储的图片
         *  3.最后将图片的相对路径地址更新回数据库中
         */

        //0.对shop的空值判断
        if(shop == null){
            return  new ShopExecution(ShopSateEnum.NULL_SHOP);//shop信息为空
        }else if(shop.getShopCategory() == null){
            return  new ShopExecution(ShopSateEnum.NULL_SHOP_CATEGORY);//ShopCategory信息为空
        }else if(shop.getArea() == null){
            return new ShopExecution(ShopSateEnum.NULL_AREA);//area信息为空
        }

        try{
            // 1.将店铺的信息插入到数据库中并返回店铺的id
            shop.setEnableStatus(0);//初始化店铺状态:0 未上架
            shop.setCreateTime(new Date());//店铺创建时间
            shop.setLastEditTime(new Date());//店铺最后一次修改时间
            // 插入数据库
            int effectedNum = shopDao.insertShop(shop);

            //判断这次添加是否有效
            if(effectedNum <= 0){
                /**
                 * 程序当且仅当抛出RuntimeException或者集成RuntimeException的子类的时候事务
                 * 才会得以终止，并回滚，如果是Exception的话事务是没办法进行回滚，该提交的就会进行提交
                 */
                throw new ShopOperationException("店铺创建失败");
            }else{
                //2.根据店铺的id去创建出存储图片的文件夹并处理存储的图片
                //2.1判断传入的文件是否为空，不为在讲店铺相关的文件存储到店铺里面
                if(shopImg != null){
                    //存储图片
                    try{
                        addShopImg(shop,shopImg);
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg error: " + e.getMessage());
                    }
                    //3.最后将图片的相对路径地址更新回数据库中
                    effectedNum = shopDao.updateShop(shop);
                    if(effectedNum <=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            //处理异常
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        //返回一个店铺的数据传输成功状态
        return new ShopExecution(ShopSateEnum.CHECK,shop);
    }

    //添加图片
    private void addShopImg(Shop shop, File shopImg) throws IOException {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        //存储图片并返回相应的相对值路径
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg,dest);
        //更改图片地址
        shop.setShopImg(shopImgAddr);
    }
}
