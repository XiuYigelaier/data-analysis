package com.example.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



@Configuration
public class RabbitConfig {

    @Bean
    public Queue calculateQueue(){
        return  new Queue("queue.calculate");
    }
}
