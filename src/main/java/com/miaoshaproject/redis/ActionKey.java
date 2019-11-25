package com.miaoshaproject.redis;

/**
 *
 */
public class ActionKey extends BasePrefix {
   private ActionKey(int expireSecond, String prefix){
       super(expireSecond,prefix);
   }

   public static ActionKey access = new ActionKey(5,"as");

}
