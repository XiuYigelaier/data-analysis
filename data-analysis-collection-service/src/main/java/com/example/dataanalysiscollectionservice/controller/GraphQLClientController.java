package com.example.dataanalysiscollectionservice.controller;

import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysiscollectionservice.service.impl.GraphQLSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gitInfo")
public class GraphQLClientController {

    @Autowired
    GraphQLSearchServiceImpl graphQLSearchServiceImpl;

    //获取用户所有仓库列表
    @GetMapping("/projectUser")
    public ResponseModel<?> graphqlSearch(@RequestParam String developerName) {
        try {
            return ResponseModel.success(graphQLSearchServiceImpl.graphqlSearch(developerName));
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" +  e.getMessage());
        }
    }

}
