package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.dao.MiaoshaUserMapper;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.util.ValidateUtil;
import com.miaoshaproject.vo.LoginVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginContorller {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
//        log.info(loginVo.toString());
//        //登录
//        String token = userService.login(response, loginVo);
//        return Result.success(token);
        String passInput = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(!ValidateUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        CodeMsg codeMsg = miaoshaUserService.login(loginVo);
        if(codeMsg.getCode()==0){
            return Result.success("登陆成功");
        }
        return Result.error(codeMsg);
    }

}
