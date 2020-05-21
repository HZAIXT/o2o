package com.zhou.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断验证码是否符合预期
 */
public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request){
        //从会话中获取正确的验证码
        String verifyCodeExpected = (String)request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //通过工具类获取前端的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request,"verifyCodeActual");
        //比较
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            return false;
        }
        return true;
    }
}
