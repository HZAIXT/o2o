package com.zhou.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 管理店铺转发的控制器
 */
@Controller
@RequestMapping(value = "/shopadmin",method = RequestMethod.GET)
public class ShopAdminController {

    /**
     * 转发访问html地址的方法
     * @return 对应html的路径
     */
    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        //已经在spring-web.xml设置了视图解析器前缀为/WEB-INF/html 后缀为.html
        //和下面的返回路径结合，按照对应的路由就能访问到/WEB-INF/html/shop/shopoperation.html了
        return "shop/shopoperation";
    }
}
