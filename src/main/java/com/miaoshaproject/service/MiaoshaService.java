package com.miaoshaproject.service;

import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.domain.OrderInfo;
import com.miaoshaproject.redis.MiaoshaKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.util.MD5Util;
import com.miaoshaproject.util.UUIDUtil;
import com.miaoshaproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	RedisService redisService;
	public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {
		boolean success = goodsService.reduceStock(goodsVo);
		if(success){
			OrderInfo orderinfo = orderService.createOrder(miaoshaUser,goodsVo);
			return orderinfo;
		}else {
			setGoodsOver(goodsVo.getId());
			return null;
		}


	}



	public long getMiaoshaResult(Long id, long goodsId) {
		MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(id,goodsId);
		if(miaoshaOrder!=null){
			return miaoshaOrder.getId();
		}else{
			boolean isOver = getGoodsOver(goodsId);
			if(isOver){
				return -1;
			}else {
				return 0;
			}
		}
    }
	private void setGoodsOver(Long id) {
		redisService.set(MiaoshaKey.isGoodsOVver,""+id,true);
	}
	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(MiaoshaKey.isGoodsOVver,""+goodsId);
	}

	public boolean checkPath(String path, MiaoshaUser miaoshaUser, long goodsId) {
		if(miaoshaUser == null || path == null) {
			return false;
		}
		String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, ""+miaoshaUser.getId() + "_"+ goodsId, String.class);
		return path.equals(pathOld);
//		String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath,""+id+"_"+goodsId,String.class);
	}


	public String createMiaoshaPath(MiaoshaUser miaoshaUser, long goodsId) {
		String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
		redisService.set(MiaoshaKey.getMiaoshaPath,""+miaoshaUser.getId()+"_"+goodsId,str);
		return str;
	}

	//生成验证码的图形框代码
	public BufferedImage createVerifyCode(MiaoshaUser miaoshaUser, long goodsId) {
		if(miaoshaUser == null || goodsId <=0) {
			return null;
		}
		int width = 80;
		int height = 32;
		//create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId()+","+goodsId, rnd);
		//输出图片
		return image;

	}
	//计算公式生成
	private int calc(String verifyCode) {
		try{
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
			return (Integer)scriptEngine.eval(verifyCode);
		}catch (Exception e){
			return 0;
		}
	}

	private static  char[] ops = new char[]{'+','-','*'};
	//随机生成3个数字，做 加减乘法
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
		int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = ""+num1+op1+num2+op2+num3;
		return exp;
	}

	//验证验证码方法
	public boolean checkVerifyCode(MiaoshaUser miaoshaUser, long goodsId, int verifyCode) {
		if(miaoshaUser == null || goodsId <=0) {
			return false;
		}
		Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId()+","+goodsId, Integer.class);
		if(codeOld == null || codeOld - verifyCode != 0 ) {
			return false;
		}
		redisService.del(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId()+","+goodsId);
		return true;
	}
}
