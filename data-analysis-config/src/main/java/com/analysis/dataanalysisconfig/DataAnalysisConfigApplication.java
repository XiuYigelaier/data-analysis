package com.analysis.dataanalysisconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@EnableDiscoveryClient
@SpringCloudApplication
public class DataAnalysisConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisConfigApplication.class, args);
    }

}
