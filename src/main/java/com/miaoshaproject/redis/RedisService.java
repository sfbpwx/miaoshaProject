package com.miaoshaproject.redis;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeysPrefix keysPrefix,String key,Class<T> tClass){
        Jedis jedis = null;
        try{
           jedis = jedisPool.getResource();
           String realKey = keysPrefix.getPrefix()+key;
           String str = jedis.get(realKey);
           T t = stringToBean(str,tClass);
           return t;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean del(KeysPrefix keysPrefix,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            long flag = jedis.del(keysPrefix.getPrefix()+key);
            return flag>0;
        }finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeysPrefix keysPrefix,String key,T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(StringUtils.isEmpty(str))return false;
            jedis.set(keysPrefix.getPrefix()+key,str);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T tClass) {
        if(tClass==null){
            return null;
        }
        Class<?> clazz = tClass.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+tClass;
        }else if(clazz==Long.class||clazz==long.class){
            return ""+tClass;
        }else if(clazz==String.class){
            return ""+tClass;
        }else{
            return JSON.toJSONString(tClass);
        }
    }

    public static <T> T stringToBean(String str,Class<T> tClass) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        if(tClass==int.class||tClass==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(tClass==Long.class||tClass==long.class){
            return (T)Long.valueOf(str);
        }else if(tClass==String.class){
            return (T)str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),tClass);
        }
    }

    private void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
