package com.miaoshaproject.service;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.dao.MiaoshaUserMapper;
import com.miaoshaproject.dataobject.MiaoshaUser;
import com.miaoshaproject.exception.GlobalException;
import com.miaoshaproject.redis.MiaoshaUserKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.util.MD5Util;
import com.miaoshaproject.util.UUIDUtil;
import com.miaoshaproject.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;

    @Autowired
    RedisService redisService;
    public MiaoshaUser getByLoginName(LoginVo loginVo){
       MiaoshaUser miaoshaUser = miaoshaUserMapper.selectByLoginName(loginVo.getMobile());
       return miaoshaUser;
    }

    public CodeMsg login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo==null)return CodeMsg.SERVER_ERROR;
        String mobile =  loginVo.getMobile();
        String passWord = loginVo.getPassword();
        MiaoshaUser miaoshaUser = miaoshaUserMapper.selectByPrimaryKey(Long.valueOf(mobile));
        if(miaoshaUser==null){
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        //验证密码
        String DBpassWord = miaoshaUser.getPassword();
        String DBsalt = miaoshaUser.getSalt();
        if(!(DBpassWord.equals(MD5Util.formPassToDBPass(passWord,DBsalt)))){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //cookie相关
        addCookie(miaoshaUser,response);
        return CodeMsg.SUCCESS;
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        //延长有效期
        addCookie(miaoshaUser,response);
        return miaoshaUser;
    }
    public void addCookie(MiaoshaUser miaoshaUser,HttpServletResponse response){
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
