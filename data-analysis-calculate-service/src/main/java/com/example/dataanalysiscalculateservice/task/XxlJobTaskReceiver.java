package com.example.dataanalysiscalculateservice.task;

import com.alibaba.fastjson.JSONObject;
import com.example.core.pojo.dto.DeveloperCollectionTranDTO;
import com.example.dataanalysiscalculateservice.service.CalculateService;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue.calculate")
public class XxlJobTaskReceiver {
    @Autowired
    CalculateService calculateService;

    @RabbitHandler
    public void receiver(Message message, Channel c, String s) throws Exception {

        MessageProperties properties = message.getMessageProperties();
        DeveloperCollectionTranDTO developerCollectionTranDTO = JSONObject.parseObject(s, DeveloperCollectionTranDTO.class );
        calculateService.calculate(developerCollectionTranDTO);
    }


}
