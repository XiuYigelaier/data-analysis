package com.example.dataanalysiscalculateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.example.core.repository","com.example.dataanalysiscalculateservice.*"})
@EntityScan(basePackages = {"com.example.core.entity","com.example.*"})
@ComponentScan({"com.example.*","com.example.dataanalysiscalculateservice.*"})
@EnableFeignClients(basePackages = "com.example.dataanalysiscalculateservice.feign")
public class DataAnalysisCalculateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisCalculateServiceApplication.class, args);
    }

}
