package com.analysis.dataanalysisservice.service.impl;

import com.analysis.dataanalysisservice.pojo.entity.TEST;
import com.analysis.dataanalysisservice.repository.TestRepository;
import com.example.core.entity.User;
import com.example.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class testServiceImpl {
     @Autowired
     RedisUtil redisUtil;

    @Autowired
    TestRepository testRepository;




    public Object set(){
        SecurityContext ctx = SecurityContextHolder.getContext();

        User u = new User();
        u.setPassword("1");
        testRepository.save(new TEST());
        redisUtil.set("1",u ,1000L);
        System.out.println(1);
        return redisUtil.get("1");
    }
}
