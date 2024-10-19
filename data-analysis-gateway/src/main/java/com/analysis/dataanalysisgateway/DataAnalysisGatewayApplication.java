package com.analysis.dataanalysisgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan({"com.analysis.dataanalysisgateway","com.example.core.*"})
public class DataAnalysisGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisGatewayApplication.class, args);
    }

}
