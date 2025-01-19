package com.analysis.dataanalysisgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.analysis.dataanalysisgateway.config"})
public class DataAnalysisGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisGatewayApplication.class, args);
    }

}
