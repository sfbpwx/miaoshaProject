package com.miaoshaproject.service;

import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {
		goodsService.reduceStock(goodsVo);

		OrderInfo orderinfo = orderService.createOrder(miaoshaUser,goodsVo);

		return orderinfo;
	}
}
