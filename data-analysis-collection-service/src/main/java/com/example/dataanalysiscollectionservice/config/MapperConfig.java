package com.example.dataanalysiscollectionservice.config;


import com.example.dataanalysiscollectionservice.mapper.DeveloperGraphMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public DeveloperGraphMapper developerGraphMapper() {
        return Mappers.getMapper(DeveloperGraphMapper.class);
    }
}