package com.miaoshaproject.redis;

/**
 *
 */
public class ActionKey extends BasePrefix {
   private ActionKey(int expireSecond, String prefix){
       super(expireSecond,prefix);
   }

//   public static ActionKey access = new ActionKey(5,"as");
    //丢入expiredTime
   public static ActionKey expired (int expiredTime){
       return new ActionKey(expiredTime,"access");
   }

}
