package com.example.dataanalysiscollectionservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.Entity;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.example.core.repository","com.example.dataanalysiscollectionservice.*"})
@EntityScan(basePackages = {"com.example.core.entity","com.example.dataanalysiscollectionservice.*"})
@ComponentScan({"com.example.*","com.example.dataanalysiscollectionservice.*"})
@EnableAsync
@EnableTransactionManagement
@EnableRabbit
public class DataAnalysisCollectionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisCollectionServiceApplication.class, args);
    }

}
