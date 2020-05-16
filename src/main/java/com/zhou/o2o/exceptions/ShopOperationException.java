package com.zhou.o2o.exceptions;

/**
 * 店铺操作异常类
 * 这样只是对RuntimeException很薄的封装，但看到这样的异常
 * 能够知道是和店铺相关
 */
public class ShopOperationException extends  RuntimeException{


    private static final long serialVersionUID = 8239429277078064769L;

    public ShopOperationException(String msg){
        super(msg);
    }
}
