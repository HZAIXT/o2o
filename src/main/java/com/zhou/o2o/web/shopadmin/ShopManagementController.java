package com.zhou.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.PersonInfo;
import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.enums.ShopSateEnum;
import com.zhou.o2o.service.ShopService;
import com.zhou.o2o.util.HttpServletRequestUtil;
import com.zhou.o2o.util.ImageUtil;
import com.zhou.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现店铺管理的相关逻辑
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;

    /**
     * 店铺注册
     * @param request 客户端的请求
     * @return
     */
    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object>  registerShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //1.接收前端传来的店铺信息并转换成对应的实体类，并且接收到前端传过来的文件流接收到shopImg里面去
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        //通过jackson-databind将POJO和JOSIN之间进行转换
        //首先构造一个ObjectMapper基础对象
        ObjectMapper mapper  = new ObjectMapper();
        Shop shop = null;
        try{
            //使用readValue方法将JOSIN转换成POJO对象
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        //使用spring自带的CommonsMultipartFile来接收图片
        CommonsMultipartFile shopImg = null;
        //使用CommonsMultipartResolver文件上传解析器去从本次会话中的上下文去获取相关文件上传的内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断该request中是否有上传的文件流
        if(commonsMultipartResolver.isMultipart(request)){
            //如果有上传的文件流就需要将request转传成MultipartHttpServletRequest对象，从该对象中提前出相对应的文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            // 直接从前端属性 "shopImg" 中获取文件流，并使用 CommonsMultipartFile来接收
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else {
            //因为是必须上传图片的，如果不具备文件流就报错
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return  modelMap;
        }

        //2.注册店铺
        if(shop != null && shopImg != null){
            //对shop对象中的PersonInfo属性进行填充
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            //店铺注册
            ShopExecution se = null;
            try {
                //getOriginalFilename():获取原本文件的名字
                se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                //判断店铺传输状态对象中的创建状态是否为创建成功
                if(se.getState() == ShopSateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
            return modelMap;
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return  modelMap;
        }
    }


    /**
     * 将InputStream类型转换成File类型
     */
 /*   private static void inputStreamToFile(InputStream ins, File file){
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead=0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer))!= -1){
                os.write(buffer,0,bytesRead);
            }
        }catch (Exception e){
            throw  new  RuntimeException("调用inputStreamToFile产生异常: "+e.getMessage());
        }finally {
            try{
                if(os != null){
                    os.close();
                }
                if(ins != null){
                    ins.close();
                }
            }catch (IOException e){
                throw  new  RuntimeException("inputStreamToFile关闭产生异常：" + e.getMessage());
            }
        }

    }*/
}
