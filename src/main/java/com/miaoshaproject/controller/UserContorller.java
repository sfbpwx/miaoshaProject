package com.miaoshaproject.controller;

import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.redis.UserKey;
import com.miaoshaproject.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserContorller {
    @Autowired
    RedisService redisService;
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<String> redisGet(){
//        redisService.set("1","hello");
        String v1 = redisService.get(UserKey.getById,"1",String.class);
        return Result.success("11111");
    }

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser miaoshaUser){
        return Result.success(miaoshaUser);
    }
}
