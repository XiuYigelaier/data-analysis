package com.example.dataanalysiscalculateservice.task;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "queue.calculate")
public class XxlJobTaskReceiver {
    @RabbitHandler
    public void receiver(Map message){
        System.out.println(message);
    }

}
