package com.miaoshaproject.service;

import com.miaoshaproject.dao.MiaoshaUserMapper;
import com.miaoshaproject.dataobject.MiaoshaUser;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.util.MD5Util;
import com.miaoshaproject.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {
    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;
    public MiaoshaUser getByLoginName(LoginVo loginVo){
       MiaoshaUser miaoshaUser = miaoshaUserMapper.selectByLoginName(loginVo.getMobile());
       return miaoshaUser;
    }

    public CodeMsg login(LoginVo loginVo){
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
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}
