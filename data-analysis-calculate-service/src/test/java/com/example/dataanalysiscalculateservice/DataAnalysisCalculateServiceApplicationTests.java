package com.example.dataanalysiscalculateservice;

import com.example.dataanalysiscalculateservice.repository.neo4j.DeveloperGraphRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataAnalysisCalculateServiceApplication.class)
@Slf4j
class DataAnalysisCalculateServiceApplicationTests {
    @Autowired
    DeveloperGraphRepository developerGraphRepository;

    @Test
    void contextLoads() {
    }

}
