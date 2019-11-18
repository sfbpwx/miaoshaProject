package com.miaoshaproject.service;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.dao.MiaoshaUserMapper;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.exception.GlobalException;
import com.miaoshaproject.localdao.MiaoshaUserDao;
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
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public CodeMsg login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo==null)return CodeMsg.SERVER_ERROR;
        String mobile =  loginVo.getMobile();
        String passWord = loginVo.getPassword();
        MiaoshaUser miaoshaUser = miaoshaUserDao.getById(Long.valueOf(mobile));
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

    public MiaoshaUser getById(long id) {
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if(miaoshaUser!=null){
            return miaoshaUser;
        }
        miaoshaUser = miaoshaUserDao.getById(id);
        if(miaoshaUser!=null){
            redisService.set(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        }
        //延长有效期
        return miaoshaUser;
    }

    public boolean updatePassword(long id,String passwordNew){
        MiaoshaUser miaoshaUser = getById(id);
        if(miaoshaUser==null){
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew,miaoshaUser.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        redisService.del(MiaoshaUserKey.getById,""+id);
        miaoshaUser.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,""+id,miaoshaUser);

        return true;
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
