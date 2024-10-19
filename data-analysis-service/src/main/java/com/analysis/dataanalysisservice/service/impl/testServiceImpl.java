package com.analysis.dataanalysisservice.service.impl;

import com.example.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class testServiceImpl {
    @Autowired
    RedisUtil redisUtil;

    public Object set(){
        redisUtil.set("1","4",1000L);
        return redisUtil.get("1");
    }
}
