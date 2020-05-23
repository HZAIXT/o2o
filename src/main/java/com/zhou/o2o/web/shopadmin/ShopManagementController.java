package com.zhou.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.o2o.dto.ShopExecution;
import com.zhou.o2o.entity.Area;
import com.zhou.o2o.entity.PersonInfo;
import com.zhou.o2o.entity.Shop;
import com.zhou.o2o.entity.ShopCategory;
import com.zhou.o2o.enums.ShopSateEnum;
import com.zhou.o2o.exceptions.ShopOperationException;
import com.zhou.o2o.service.AreaService;
import com.zhou.o2o.service.ShopCategoryService;
import com.zhou.o2o.service.ShopService;
import com.zhou.o2o.util.CodeUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现店铺管理的相关逻辑
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 获取店铺分类信息以及店铺区域信息
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<String,Object>();

        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();

        try{
            //获取全部Category
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            //获取全部Area
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    /**
     * 店铺注册
     * @param request 客户端的请求
     * @return
     */
    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object>  registerShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //判断验证码
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入错误的验证码");
            return modelMap;
        }

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
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            //店铺注册
            ShopExecution se = null;
            try {
                //getOriginalFilename():获取原本文件的名字
                se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                //判断店铺传输状态对象中的创建状态是否为创建成功
                if(se.getState() == ShopSateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    //该用户可以操作的店铺列表
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if(shopList == null || shopList.size() == 0){
                        //表明这是他第一个创建的店铺
                        shopList = new ArrayList<Shop>();
                    }
                    //不是第一次创建店铺
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
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
     * 根据店铺的id获取对应的店铺信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //从前端获取ShopId
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if(shopId > -1){
            try {
                //获取店铺信息
                Shop shop = shopService.getByShopId(shopId);
                //获取区域列表的信息
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    /**
     * 修改店铺信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyshop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        // 2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se;
            try {
                if(shopImg == null){
                    //当文件为空也就是不修改图片的时候
                    se = shopService.modifyShop(shop, null, null);
                }else{
                    se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }
                //判断操作状态
                if(se.getState() == ShopSateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺Id");
            return modelMap;
        }
    }

}
