package com.example.dataanalysissecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.example.core.repository"})
@EntityScan(basePackages = {"com.example.core.pojo","com.example.auth.pojo"})
@ComponentScan({"com.example.dataanalysissecurity","com.example.*"})
@SpringBootApplication
public class DataAnalysisSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisSecurityApplication.class, args);
    }

}
