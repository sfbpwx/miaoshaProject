package com.miaoshaproject.service;

import com.miaoshaproject.dao.MiaoshaUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {
    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;
}
