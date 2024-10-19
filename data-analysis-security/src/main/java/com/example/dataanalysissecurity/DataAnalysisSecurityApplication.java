package com.example.dataanalysissecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan({"com.example.dataanalysissecurity","com.example.core.*"})
@SpringBootApplication
public class DataAnalysisSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisSecurityApplication.class, args);
    }

}
