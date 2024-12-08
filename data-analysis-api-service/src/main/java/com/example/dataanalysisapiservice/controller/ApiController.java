package com.example.dataanalysisapiservice.controller;

import com.example.core.entity.ResponseModel;
import com.example.dataanalysisapiservice.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/api")
public class ApiController {
    @Autowired
    ApiService apiService;

    @GetMapping("/search")
    public ResponseModel<?> calculateRank(@RequestParam String login,   Boolean updateFlag) {
        try {
            apiService.calculateRank(login,updateFlag);
            return ResponseModel.success("成功");
        } catch (Exception e) {
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
}
