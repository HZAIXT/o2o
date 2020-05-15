package com.zhou.o2o.web.superadmin;

import com.zhou.o2o.entity.Area;
import com.zhou.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
    //调用logback组件
    Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    /**
     * 返回区域列表的方法
     * @return 区域列表
     */
    @RequestMapping(value = "/listarea",method = RequestMethod.GET)
    @ResponseBody
    private  Map<String,Object> listArea(){
        //在INFO级别测试信息
        logger.info("===start===");
        //方法执行时间
        long startTime = System.currentTimeMillis();

        //存放各项返回数据
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //获取service层返回的区域列表
        List<Area> list = new ArrayList<Area>();
        try{
            list = areaService.getAreaList();
            modelMap.put("rows",list);//结果
            modelMap.put("total",list.size());//区域数量

        }catch(Exception e){
            e.printStackTrace();
            //执行状态： true 成功 false 失败
            modelMap.put("success",false);
            //错误信息
            modelMap.put("errMsg",e.toString());
        }

        //error级别日志测试信息
        logger.error("test error!");
        //程序结束时间
        long endTime = System.currentTimeMillis();
        //debug级别日志信息测试
        logger.debug("costTime:[{}ms]",endTime-startTime);
        //info级别日志信息测试
        logger.info("===end===");
        return modelMap;
    }
}
