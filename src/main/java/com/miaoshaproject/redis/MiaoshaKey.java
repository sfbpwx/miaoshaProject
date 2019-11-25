package com.miaoshaproject.redis;

/**
 *
 */
public class MiaoshaKey extends BasePrefix {
   private MiaoshaKey(int expiredSecond,String prefix){
       super(expiredSecond,prefix);
   }
   public static MiaoshaKey isGoodsOVver = new MiaoshaKey(0,"go");
   public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60,"mp");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300,"vc");

}
