package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.dao.MiaoshaUserMapper;
import com.miaoshaproject.dataobject.MiaoshaUser;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.vo.LoginVo;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/goods")
public class GoodsContorller {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_list")
    public String toLogin(Model model,MiaoshaUser miaoshaUser)
//                          @CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
//                          @RequestParam(value=MiaoshaUserService.COOKIE_NAME_TOKEN, required = false)String parameToken){
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(parameToken))
        {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(parameToken)?cookieToken:parameToken;
//        MiaoshaUser miaoshaUser = miaoshaUserService.getByToken(response,token);
        model.addAttribute("user",miaoshaUser);
        return "goods_list";
    }

    @RequestMapping("/to_detail")
    public String toDetail(HttpServletResponse response,Model model,
//                           @CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
//                          @RequestParam(value=MiaoshaUserService.COOKIE_NAME_TOKEN, required = false)String parameToken,
                           MiaoshaUser miaoshaUser){
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(parameToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(parameToken)?cookieToken:parameToken;
//        MiaoshaUser miaoshaUser = miaoshaUserService.getByToken(response,token);
        model.addAttribute("user",miaoshaUser);
        return "goods_detail";
    }

}
