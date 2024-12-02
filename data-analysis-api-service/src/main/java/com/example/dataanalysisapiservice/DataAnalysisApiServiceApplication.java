package com.example.dataanalysisapiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.example.core.repository","com.example.dataanalysisapiservice.*"})
@EntityScan(basePackages = {"com.example.core.entity","com.example.dataanalysiscollectionservice.*"})
@ComponentScan({"com.example.*","com.example.dataanalysisapiservice.*"})
@EnableAsync
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.example.dataanalysisapiservice.feign")
public class DataAnalysisApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisApiServiceApplication.class, args);
    }

}
