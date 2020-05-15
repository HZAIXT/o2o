package com.zhou.o2o.dto;

import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.enums.ShopSateEnum;

import java.util.List;

/**
 * 店铺的数据传输类
 */
public class ShopExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop(增删改店铺的时候要用到)
    private Shop shop;

    //shop列表（查询店铺列表的时候使用)
    private List<Shop> shopList;

    //无参构造函数
    public ShopExecution(){}

    //带参构造：店铺操作失败的时候使用的构造器
    public ShopExecution(ShopSateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //带参构造：店铺操作成功的时候使用的构造器  返回单个shop
    public ShopExecution(ShopSateEnum stateEnum,Shop shop){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }

    //带参构造: 店铺操作成功的时候使用的构造器  返回一个shopList
    public ShopExecution(ShopSateEnum stateEnum,List<Shop> shopList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
