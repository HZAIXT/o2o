package com.zhou.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 解析HttpServletRequest里面的一些参数，并将对应值转换为具体类型的工具类
 */
public class HttpServletRequestUtil {

    /**
     * 将对应key中的value值进行整形转换
     * @param request 客户端的请求
     * @param key   请求中的各种key中的值
     * @return  转换后的整形参数
     */
    public static  int getInt(HttpServletRequest request,String key){
        try {
            return Integer.decode(request.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }

    /**
     * 将对应key中的value值进行长整形转换
     * @param request 客户端的请求
     * @param key   请求中的各种key中的值
     * @return  转换后的长整形参数
     */
    public static long getLong(HttpServletRequest request,String key){
        try {
            return Long.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }

    /**
     * 将对应key中的value值进行Double类型转换
     * @param request 客户端的请求
     * @param key   请求中的各种key中的值
     * @return  转换后的Double类型参数
     */
    public static  Double getDouble(HttpServletRequest request,String key){
        try {
            return Double.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1d;
        }
    }

    /**
     * 将对应key中的value值进行Boolean类型的转换
     * @param request 客户端的请求
     * @param key   请求中的各种key中的值
     * @return  转换后的Boolean类型参数
     */
    public static boolean getBoolean(HttpServletRequest request,String key){
        try {
            return Boolean.valueOf(request.getParameter(key));
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 将对应key中的value值进行String类型的转换
     * @param request 客户端的请求
     * @param key   请求中的各种key中的值
     * @return  转换后的String类型参数
     */
    public static String getString(HttpServletRequest request,String key){
        try {
            String  result =  request.getParameter(key);
            //如果字符串不为空 就将它两侧的空格去掉
            if(result != null){
                result = result.trim();
            }
            //如果字符串为"",就将它置为null
            if("".equals(result)){
                result = null;
            }
            return  result;
        }catch (Exception e){
            return null;
        }
    }
}
