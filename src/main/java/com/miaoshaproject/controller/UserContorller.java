package com.miaoshaproject.controller;

import com.miaoshaproject.dataobject.User;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.redis.UserKey;
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
        String v1 = redisService.get(UserKey.getById,"1",String.class);
        return Result.success(v1);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("11111");
        return Result.success(redisService.set(UserKey.getById,"1",user));
    }
}
