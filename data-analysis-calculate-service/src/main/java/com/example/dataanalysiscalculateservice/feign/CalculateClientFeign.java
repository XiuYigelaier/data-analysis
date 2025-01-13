package com.example.dataanalysiscalculateservice.feign;

import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysiscalculateservice.config.FeignClientInterceptor;
import com.example.core.pojo.dto.DeveloperCollectionTranDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//value = "data-analysis-collection-service"
@Service
@Component
@FeignClient(value = "DATA-ANALYSIS-COLLECTION-SERVICE",configuration = FeignClientInterceptor.class)
public interface CalculateClientFeign {

    @GetMapping("/api/gitInfo/findAll")
    public ResponseModel<List<DeveloperCollectionTranDTO>> searchAllDeveloper();


    @GetMapping("/api/gitInfo/findByLogin")
    public ResponseModel<DeveloperCollectionTranDTO> findByProjectUser(@RequestParam("login") String login);

}
