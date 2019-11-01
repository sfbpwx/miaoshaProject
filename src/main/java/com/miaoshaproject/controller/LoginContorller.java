package com.miaoshaproject.controller;

import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginContorller {
    @Autowired
    RedisService redisService;


    @RequestMapping("/to_login")
    public String toLogin(){
        return "login.html";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> redisGet(){
//        redisService.set("1","hello");
//        String v1 = redisService.get("1",String.class);
        return Result.success("11111");
    }
}
