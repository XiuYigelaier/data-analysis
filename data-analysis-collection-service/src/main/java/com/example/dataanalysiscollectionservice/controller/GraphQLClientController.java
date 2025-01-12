package com.example.dataanalysiscollectionservice.controller;

import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysiscollectionservice.pojo.vo.DeveloperCollectionVO;
import com.example.dataanalysiscollectionservice.service.impl.GraphQLSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gitInfo")
public class GraphQLClientController {

    @Autowired
    GraphQLSearchServiceImpl graphQLSearchServiceImpl;

    //获取用户所有仓库列表
    @GetMapping("/findAll")
    public ResponseModel<List<DeveloperCollectionVO>> findAll( ) {
        try {
            return ResponseModel.success(graphQLSearchServiceImpl.findAll());
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" +  e.getMessage());
        }
    }


    @GetMapping("/findByLogin")
    public ResponseModel<DeveloperCollectionVO> findByProjectUser(@RequestParam("login")String login) {
        try {
            return ResponseModel.success(graphQLSearchServiceImpl.findByLogin(login));
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" +  e.getMessage());
        }
    }
    @GetMapping("/graphqlSearch")
    public void graphqlSearch( String projectUser) {
        try {
            graphQLSearchServiceImpl.graphqlSearch(projectUser);
        } catch (Exception e) {
        }
    }

}
