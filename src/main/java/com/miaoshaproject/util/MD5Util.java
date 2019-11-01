package com.miaoshaproject.util;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md2Hex(src);
    }

    private static final String salt="1a2b3c4d";

    public static String inputPassFromPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formpass,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2)+formpass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String input,String saltDB){
        String formPass = inputPassFromPass(input);
        String dbPass = formPassToDBPass(formPass,saltDB);
        return dbPass;
    }

    public static void main(String args[]){
//        System.out.println(inputPassFromPass("123456"));
//        System.out.println(formPassToDBPass("123456","1a2b3c4d"));
        inputPassToDBPass("123456","1a2b3c");
    }
}
