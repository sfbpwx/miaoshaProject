package com.miaoshaproject.access;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

//HandlerInterceptorAdapter 拦截器基类
public class AccessInterCeptor  extends HandlerInterceptorAdapter {

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(handler instanceof  HandlerMethod){
            MiaoshaUser miaoshaUser = getUser(request,response);
            UserContext.setUser(miaoshaUser);


            HandlerMethod handlerMethod = (HandlerMethod)handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return true;
            }
//            MiaoshaUser miaoshaUser = redis
            int seconds = accessLimit.seconds();
            int macCount = accessLimit.macCount();
            boolean needLogin = accessLimit.needLogin();
            if(needLogin){
                if(miaoshaUser==null){
                    render(response,CodeMsg.SERVER_ERROR);
                    return false;
                }
                key += "_"+miaoshaUser.getId();
            }else{

            }
            if(count==null){

            }
        }


        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg)throws Exception{
        OutputStream outputStream  = response.getOutputStream();
        String str = JSON.toJSONString(codeMsg);
        outputStream.write(str.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private MiaoshaUser getUser(HttpServletRequest request,HttpServletResponse response){
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken)&& StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaoshaUserService.getByToken(response,token);
    }
}
