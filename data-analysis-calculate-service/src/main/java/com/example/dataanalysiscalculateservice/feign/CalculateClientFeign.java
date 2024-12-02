package com.example.dataanalysiscalculateservice.feign;

import com.example.core.entity.ResponseModel;
import com.example.dataanalysiscalculateservice.config.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//value = "data-analysis-collection-service"
@Service
@Component
@FeignClient(value = "DATA-ANALYSIS-COLLECTION-SERVICE",configuration = FeignClientInterceptor.class)
public interface CalculateClientFeign {

    @GetMapping("/api/gitInfo/projectUser")
    public ResponseModel<?> graphqlSearch(@RequestParam("developerName") String developerName);

}
