package com.zhou.o2o.util;

/**
 * 为ImageUtil中的需求提供路径
 */
public class PathUtil {
    //获取当前系统的文件分隔符
    private static String seperator = System.getProperty("file.separator");


    /**
     * 根据环境不同，提供不同的根路径
     * @return 根路径
     */
    public static String getImgBasePath(){
        //获取操作系统的名称
        String os = System.getProperty("os.name");
        String basePath = "";
        //根据不同的操作系统选择不同的存储路径
        if(os.toLowerCase().startsWith("win")){
            basePath = "B:/projectdev/image/";
        }else{
            basePath = "/home/huangzhou/image/";
        }

        //将存储路径的文件分割符与对应系统的分隔符进行替换
        basePath = basePath.replace("/",seperator);

        return basePath;
    }

    /**
     * 店铺图片存储路径  图片要分别存储在各自图片的路径下
     * @return  图片存储路径
     */
    public static String getShopImagePath(long shopId){
        String imagePath = "upload/item/shop/" + shopId + "/";
        return imagePath.replace("/",seperator);
    }
}
