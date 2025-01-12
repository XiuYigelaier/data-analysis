package com.example.dataanalysisapiservice.feign;
import  com.example.dataanalysisapiservice.config.FeignClientInterceptor;
import com.example.core.pojo.base.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//value = "data-analysis-collection-service"
@Service
@Component
@FeignClient(value = "DATA-ANALYSIS-CALCULATE-SERVICE",configuration = FeignClientInterceptor.class)
public interface CollectionClientFeign {

    @GetMapping("/api/calculate/projectUser")
    public ResponseModel<?> calcuate(@RequestParam("projectUser") String projectUser);

    @GetMapping("/api/calculate/pagrRank")
    public ResponseModel<?> pageRank();


}
