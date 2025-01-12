package com.example.dataanalysisapiservice.controller;

import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysisapiservice.service.ApiService;
import org.neo4j.driver.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/api")
public class ApiController {
    @Autowired
    ApiService apiService;

    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

    @GetMapping("/search")
    public ResponseModel<?> calculateRank(@RequestParam String login,   Boolean updateFlag) {
        try {

            return ResponseModel.success(apiService.calculateRank(login,updateFlag));
        } catch (Exception e) {
            log.error("计算是失败：",e);

            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseModel<?> findAll(){
        try{
            return ResponseModel.success(apiService.findAll());
        } catch (Exception e){
            return ResponseModel.failure("获取失败" +  e.getMessage());
        }
    }

    @GetMapping("/pageRank")
    public ResponseModel<?> pageRank(){
        try{
            apiService.pageRank();
            return ResponseModel.success("pageRank成功");
        } catch (Exception e){
            return ResponseModel.failure("获取失败" +  e.getMessage());
        }
    }


}
