package com.analysis.dataanalysisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.example.core.repository","com.analysis.dataanalysisservice.repository"})
@EntityScan(basePackages = {"com.example.core.entity","com.analysis.dataanalysisservice.pojo"})
@ComponentScan({"com.analysis.dataanalysisservice.*","com.example.*"})
public class DataAnalysisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisServiceApplication.class, args);
    }

}
