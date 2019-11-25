package com.miaoshaproject.rabbitmq;

import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.GoodsService;
import com.miaoshaproject.service.MiaoshaService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
    @RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
    public void receive (String message){
        log.info("receiveMessage:"+message);
        //通过redis方法获取队列信息
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message,MiaoshaMessage.class);
        //获取秒杀的user信息
        MiaoshaUser miaoshaUser = miaoshaMessage.getMiaoshaUser();
        //获取秒杀的商品ID
        long goodsId = miaoshaMessage.getGoodsId();
        //通过商品ID获取当前商品的所有内容
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        //获取商品的库存
        int stock = goodsVo.getStockCount();
        //如果库存小于0，则返回
        if(stock<0){
//            return  Result.error(CodeMsg.MIAO_SHA_OVER);
            return;
        }
        //当秒杀订单未生成时，则发返回
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(),goodsId);
        if(miaoshaOrder!=null){
//            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//            return "miaosha_fail";
            return;
        }
        //当商品库存大于0并且秒杀订单已经生成时，进行秒杀操作
        miaoshaService.miaosha(miaoshaUser,goodsVo);
    }
}
