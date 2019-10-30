package com.miaoshaproject.controller;

import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class UserContorller {
    @Autowired
    RedisService redisService;
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<String> redisGet(){
//        redisService.set("1","hello");
        String v1 = redisService.get("1",String.class);
        return Result.success(v1);
    }
}
