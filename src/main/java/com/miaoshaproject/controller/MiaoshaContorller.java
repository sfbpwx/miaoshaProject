package com.miaoshaproject.controller;

import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.service.GoodsService;
import com.miaoshaproject.service.MiaoshaService;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaContorller {
    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",miaoshaUser);
        if(miaoshaUser==null){
            return "login";
        }
        //判断商品是否有库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock<0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(),goodsId);
        if(miaoshaOrder!=null){
            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        //减库存
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser,goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order_detail";
    }
}
