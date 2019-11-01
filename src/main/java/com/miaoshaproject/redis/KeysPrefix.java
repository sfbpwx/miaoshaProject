package com.miaoshaproject.redis;

public interface KeysPrefix {
    public int expireSeconds();
    public String getPrefix();
}
