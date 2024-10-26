package com.analysis.dataanalysisservice.controller;

import com.analysis.dataanalysisservice.service.GitHubInfoService;
import com.example.core.entity.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gitInfo")
public class GitHubInfoController {

    @Autowired
    GitHubInfoService gitHubInfoService;

    //获取用户所有仓库列表
    @GetMapping("/projectUser")
    public ResponseModel<?> getRepos(@RequestParam String projectUser) {
        try {
            return ResponseModel.success(gitHubInfoService.getRepos(projectUser));
        } catch (Exception e) {
            return ResponseModel.failure("获取失败" + e);
        }
    }
@GetMapping("/calculateContribution")
    public ResponseModel<?> calculateContribution(@RequestParam String projectUser,@RequestParam String repos) {
        try {
            return ResponseModel.success(gitHubInfoService.calculateContribution(projectUser, repos));

        } catch (Exception e) {
            return ResponseModel.failure("计算失败" + e);
        }
    }

}
