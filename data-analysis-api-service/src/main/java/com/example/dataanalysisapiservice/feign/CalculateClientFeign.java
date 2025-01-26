package com.example.dataanalysisapiservice.feign;
import com.example.core.enums.ProjectClassificationEnum;
import  com.example.dataanalysisapiservice.config.FeignClientInterceptor;
import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysisapiservice.pojo.dto.ProjectClassificationDTO;
import com.example.dataanalysisapiservice.pojo.dto.TalentRankDTO;
import org.bouncycastle.LICENSE;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

//value = "data-analysis-collection-service"
@Service
@Component
@FeignClient(value = "DATA-ANALYSIS-CALCULATE-SERVICE",configuration = FeignClientInterceptor.class)
public interface CalculateClientFeign {

    @GetMapping("/api/calculate/projectUser")
    public ResponseModel<?> calcuate(@RequestParam("projectUser") String projectUser);

    @GetMapping("/api/calculate/findByLogin")
    public ResponseModel<TalentRankDTO> findByLogin(@RequestParam("login") String login);


    @GetMapping("/api/calculate/findAll")
    public ResponseModel<List<TalentRankDTO>> findAll();

    @GetMapping("/api/calculate/projectClassificationList")
    public  ResponseModel<List<ProjectClassificationDTO>> projectClassificationList();

    @GetMapping("/api/calculate/projectClassificationCount")
    public  ResponseModel<Map<ProjectClassificationEnum,Long>> projectClassificationCount();



}
