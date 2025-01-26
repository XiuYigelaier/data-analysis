package com.example.dataanalysisapiservice.controller;

import com.example.core.enums.ProjectClassificationEnum;
import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysisapiservice.pojo.vo.ProjectClassificationVO;
import com.example.dataanalysisapiservice.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class ApiController {
    @Autowired
    ApiService apiService;

    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

//    @GetMapping("/search")
//    public ResponseModel<?> calculateRank(@RequestParam String login,   Boolean updateFlag) {
//        try {
//
//            return ResponseModel.success(apiService.calculateRank(login,updateFlag));
//        } catch (Exception e) {
//            log.error("计算是失败：",e);
//
//            return ResponseModel.failure("计算失败" +  e.getMessage());
//        }
//    }

    @GetMapping("/findAll")
    public ResponseModel<?> findAll(){
        try{
            return ResponseModel.success(apiService.findAll());
        } catch (Exception e){
            log.error("api findAll失败：",e);
            return ResponseModel.failure("获取失败" +  e.getMessage());
        }
    }
    @GetMapping("/projectClassificationList")
    public  ResponseModel<List<ProjectClassificationVO>> projectClassificationList(){
        try {
            return ResponseModel.success(apiService.projectClassificationList());
        } catch (Exception e) {
            return ResponseModel.failure("获取20个不同分类项目" +  e.getMessage());
        }
    }

    @GetMapping("/projectClassificationCount")
    public  ResponseModel<Map<ProjectClassificationEnum,Long>> projectClassificationCount(){
        try {
            return ResponseModel.success(apiService.projectClassificationCount());
        } catch (Exception e) {
            return ResponseModel.failure("获取项目分类统计和失败" +  e.getMessage());
        }
    }







}
