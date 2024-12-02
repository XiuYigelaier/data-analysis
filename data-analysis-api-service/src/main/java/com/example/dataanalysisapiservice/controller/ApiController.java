package com.example.dataanalysisapiservice.controller;

import com.example.core.entity.ResponseModel;
import com.example.dataanalysisapiservice.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/api")
public class ApiController {
    @Autowired
    ApiService apiService;

    @GetMapping("/projectUser")
    public ResponseModel<?> calculateRank(@RequestParam String projectUser) {
        try {
            apiService.calculateRank(projectUser);
            return ResponseModel.success("成功");
        } catch (Exception e) {
            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }
}
