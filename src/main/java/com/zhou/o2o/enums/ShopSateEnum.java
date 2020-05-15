package com.zhou.o2o.enums;

//ShopExecution 店铺的数据传输类dto的 枚举类
public enum  ShopSateEnum {
    CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"ShopId为空"),
    NULL_SHOP(-1003,"shop信息为空"),NULL_SHOP_CATEGORY(-1004,"ShopCategory为空"),
    NULL_AREA(-1005,"area为空");

    private int state;
    private String stateInfo;

    private ShopSateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     * values()就是包含了所有枚举对象的值了
     */
    public static ShopSateEnum stateOf(int state){
        for(ShopSateEnum sateEnum:values()){
            if(sateEnum.getState()== state){
                return sateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }
    public String getStateInfo() {
        return stateInfo;
    }
}