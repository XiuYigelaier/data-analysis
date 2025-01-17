package com.example.dataanalysiscalculateservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableNeo4jRepositories("com.example.dataanalysiscalculateservice.*")
@EnableJpaRepositories(basePackages = {"com.example.dataanalysiscalculateservice.*"})
@EntityScan(basePackages = {"com.example.core.pojo","com.example.dataanalysiscalculateservice.pojo"})
@ComponentScan({"com.example.*","com.example.dataanalysiscalculateservice.*"})
@EnableFeignClients(basePackages = "com.example.dataanalysiscalculateservice.feign")
@EnableRabbit
public class DataAnalysisCalculateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisCalculateServiceApplication.class, args);
    }

}
