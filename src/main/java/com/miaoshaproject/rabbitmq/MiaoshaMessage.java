package com.miaoshaproject.rabbitmq;

import com.miaoshaproject.domain.MiaoshaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {
    MiaoshaUser miaoshaUser;
    private long goodsId;
}
