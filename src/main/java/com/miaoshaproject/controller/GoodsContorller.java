package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.dao.MiaoshaUserMapper;
import com.miaoshaproject.dataobject.MiaoshaUser;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.GoodsService;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.vo.GoodsVo;
import com.miaoshaproject.vo.LoginVo;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsContorller {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model,MiaoshaUser miaoshaUser){
        List<GoodsVo> list = goodsService.listGoodsVo();
        model.addAttribute("goodsList",list);
        model.addAttribute("user",miaoshaUser);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(MiaoshaUser user, @PathVariable("goodsId")long goodsId, Model model){
        //snowflake
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startTime ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startTime - now )/1000);
        }else  if(now > endTime){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("goods",goodsVo);
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("user",user);
        return "goods_detail";
    }

}
