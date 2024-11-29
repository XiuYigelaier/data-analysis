package com.example.dataanalysiscollectionservice.controller;

import com.example.core.entity.ResponseModel;
import com.example.dataanalysiscollectionservice.service.GitHubInfoService;
import com.example.dataanalysiscollectionservice.service.impl.GraphQLClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gitInfo")
public class GraphQLClientController {

    @Autowired
    GraphQLClient graphQLClient;

    //获取用户所有仓库列表
    @GetMapping("/projectUser")
    public ResponseModel<?> getRepos(@RequestParam String projectUser) {
        try {
            graphQLClient.graphqlSearch(projectUser);
            return ResponseModel.success("成功");
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" + e);
        }
    }

}
