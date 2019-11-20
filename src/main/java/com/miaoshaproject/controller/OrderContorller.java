package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.redis.GoodsKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.GoodsService;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.vo.GoodsDetailVo;
import com.miaoshaproject.vo.GoodsVo;
import com.miaoshaproject.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderContorller {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping("/detail")
    @ResponseBody
//    @NeedLogin
    public Result<OrderDetailVo> toList(Model model, MiaoshaUser miaoshaUser, @RequestParam("orderId")long orderId){
        if(miaoshaUser==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderInfo(orderInfo);
        vo.setGoods(goodsVo);
        return Result.success(vo);
    }


}
