package com.miaoshaproject.controller;

import com.miaoshaproject.access.AccessLimit;
import com.miaoshaproject.domain.MiaoshaOrder;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.rabbitmq.MQSender;
import com.miaoshaproject.rabbitmq.MiaoshaMessage;
import com.miaoshaproject.redis.ActionKey;
import com.miaoshaproject.redis.GoodsKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.GoodsService;
import com.miaoshaproject.service.MiaoshaService;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaContorller implements InitializingBean {
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
    @Autowired
    MQSender mqSender;
    private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();

    @RequestMapping(value="/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result doMiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId")long goodsId,
                            @PathVariable("path") String path){
        model.addAttribute("user",miaoshaUser);
        if(miaoshaUser==null){
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证获取的path是否是之前传入的path  对串进行验证
        boolean check = miaoshaService.checkPath(path,miaoshaUser,goodsId);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //判断是否小于0，当小于零的时候，对此处进行标记
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预先减少库存 Redis减少库存，此处不调用数据库内容
        long stock =  redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
        if(stock<0){
            //如果库存小于0，则修改商品的标志。后面就可以通过当前商品的标志来直接返回操作，避免重复访问redis
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到，即判断当前订单是否已经生成，此处获取秒杀订单，是通过redis的存储来获取，并非通过数据库的查询操作
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(),goodsId);
        if(miaoshaOrder!=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //此处为入队操作。通过RabbitMQ的队列进行入队操作。入队结束后，才会进行接下来的生成订单，秒杀操作
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setMiaoshaUser(miaoshaUser);
        miaoshaMessage.setGoodsId(goodsId);
        mqSender.send(miaoshaMessage);
        return Result.success(0);
        //判断商品是否有库存
//        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goodsVo.getStockCount();
//        if(stock<0){
////            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//            return  Result.error(CodeMsg.MIAO_SHA_OVER);
////            return "miaosha_fail";
//        }
        //判断是否已经秒杀到
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(),goodsId);
//        if(miaoshaOrder!=null){
////            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
////            return "miaosha_fail";
//        }
        //减库存
//        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser,goodsVo);
////        model.addAttribute("orderInfo",orderInfo);
////        model.addAttribute("goods",goodsVo);
//        return Result.success(orderInfo);
//        return "order_detail";
    }
    //系统初始化功能
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if(goodsVoList==null){
            return;
        }
        //系统初始化的过程中，对每个商品进行轮询，然后将商品状态录入到本地的内存map中
        for(GoodsVo goodsVo:goodsVoList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    /**
     *
     * orderid  成功
     * -1 失败
     * 0 排队
     * @return
     */
    @AccessLimit(seconds=5,macCount=5,needLogin=true)
    @RequestMapping(value="/result",method = RequestMethod.POST)
    @ResponseBody
    public Result getMiaoshaGoodsResult(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId")long goodsId
                           ) {
        model.addAttribute("user", miaoshaUser);
        if(miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =miaoshaService.getMiaoshaResult(miaoshaUser.getId(), goodsId);
        return Result.success(result);
    }
    @AccessLimit(seconds=5,macCount=5,needLogin=true)
    @RequestMapping(value="/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request,Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId")long goodsId,
                        @RequestParam("VerifyCode")int verifyCode) {
        model.addAttribute("user", miaoshaUser);
        if (miaoshaUser == null) {
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断用户是否访问太频繁 5秒5次  防刷
//        redisService.get(ActionKey.access,""+,clazz);
//        String uri = request.getRequestURI();
//        String key = uri+"_"+miaoshaUser.getId();
//        Integer count = redisService.get(ActionKey.access,key,Integer.class);
//        if(count ==null){
//            redisService.set(ActionKey.access,key,Integer.class);
//        }else if (count<5){
//            redisService.incr(ActionKey.access,key);
//        }else{
//            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
//        }

        boolean check = miaoshaService.checkVerifyCode(miaoshaUser,goodsId,verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //生成验证串方法
        String path  =miaoshaService.createMiaoshaPath(miaoshaUser, goodsId);
        return  Result.success(path);
    }
    @RequestMapping(value="/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, HttpServletResponse response,Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId")long goodsId
    ) {
        model.addAttribute("user", miaoshaUser);
        if (miaoshaUser == null) {
//            return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //生成验证串方法
        BufferedImage bufferedImage = miaoshaService.createVerifyCode(miaoshaUser,goodsId);
        try{
            OutputStream outputStream =response.getOutputStream();
            ImageIO.write(bufferedImage, "JPEG", outputStream);
            outputStream.flush();
            outputStream.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }

}
