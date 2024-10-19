package com.analysis.dataanalysiseureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DataAnalysisEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisEurekaApplication.class, args);
    }

}
