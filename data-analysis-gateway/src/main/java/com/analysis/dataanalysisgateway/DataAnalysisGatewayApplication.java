package com.analysis.dataanalysisgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.example.core.repository"})
@EntityScan(basePackages = {"com.example.core.entity","com.example.auth.entity","com.example.auth.*"})
@ComponentScan({"com.analysis.dataanalysisgateway","com.example.*"})
public class DataAnalysisGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisGatewayApplication.class, args);
    }

}
