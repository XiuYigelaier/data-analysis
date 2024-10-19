package com.analysis.dataanalysisservice.controller;

import com.analysis.dataanalysisservice.pojo.entity.TEST;
import com.analysis.dataanalysisservice.repository.TestRepository;
import com.analysis.dataanalysisservice.service.impl.testServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    testServiceImpl testService;

    @GetMapping("/1")
     public Object test(){
       return  testService.set();
     }



}
