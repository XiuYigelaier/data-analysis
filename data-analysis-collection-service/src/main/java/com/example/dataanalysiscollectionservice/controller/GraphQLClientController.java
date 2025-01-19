package com.example.dataanalysiscollectionservice.controller;

import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysiscollectionservice.pojo.vo.mysql.DeveloperCollectionVO;
import com.example.dataanalysiscollectionservice.pojo.vo.neo4j.DeveloperCollectionGraphVO;
import com.example.dataanalysiscollectionservice.service.impl.GraphQLSearchServiceImpl;
import com.example.dataanalysiscollectionservice.task.XxlJobTaskSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(GraphQLClientController.class);

    //获取用户所有仓库列表
    @GetMapping("/findAll")
    public ResponseModel<List<DeveloperCollectionVO>> findAll() {
        try {
            return ResponseModel.success(graphQLSearchServiceImpl.findAll());
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" + e.getMessage());
        }
    }

    @GetMapping("/findAllRelationship")
    public ResponseModel<List<DeveloperCollectionGraphVO>> findAllRelationGraph() {
        try {
             return ResponseModel.success(graphQLSearchServiceImpl.findAllRelationGraph()) ;
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" + e.getMessage());
        }
    }


    @GetMapping("/findByLogin")
    public ResponseModel<DeveloperCollectionVO> findByProjectUser(@RequestParam("login") String login) {
        try {
            log.info("findAllRelationShip:login为【"+login+"】");
            return ResponseModel.success(graphQLSearchServiceImpl.findByLogin(login));
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" + e.getMessage());
        }
    }

    @GetMapping("/graphqlSearch")
    public void findAllRelationGraph(String login) {
        try {
            log.info("graphqlSearch:login为【"+login+"】");
            graphQLSearchServiceImpl.graphqlSearch(login);
        } catch (Exception e) {
        }
    }




}
