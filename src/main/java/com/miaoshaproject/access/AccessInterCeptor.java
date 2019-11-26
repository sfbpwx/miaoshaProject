package com.miaoshaproject.access;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.redis.ActionKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

//HandlerInterceptorAdapter 拦截器基类
@Service
public class AccessInterCeptor  extends HandlerInterceptorAdapter {

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;
    @Override//防刷限流过滤器
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(handler instanceof  HandlerMethod){
            MiaoshaUser miaoshaUser = getUser(request,response);
            //user保存在usercontext里面，后面再直接从usercontext里面获取
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
            String key = request.getRequestURI();
            if(needLogin){//指标中涉及必须登录的选项
                if(miaoshaUser==null){
                    render(response,CodeMsg.SERVER_ERROR);
                    return false;//如果获取用户为空，则返回错误
                }
                key += "_"+miaoshaUser.getId();
            }else{
                // do nothing
            }
            //先将seconds丢入acitonKey，然后再把生成的actionKey丢入到后面的方法中
            ActionKey actionKey = ActionKey.expired(seconds);
            Integer count = redisService.get(actionKey,key,Integer.class);//通过redis获取当前刷新的次数
            if(count ==null){
                redisService.set(actionKey,key,Integer.class);
            }else if (count<macCount){
                redisService.incr(actionKey,key);
            }else{
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
//                return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg)throws Exception{
        response.setContentType("application/json;charset=UTF-8");//避免返回页面时产生乱码
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
//            return "login";
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaoshaUserService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length==0){
            return null;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
