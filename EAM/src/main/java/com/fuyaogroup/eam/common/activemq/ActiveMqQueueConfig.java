package com.fuyaogroup.eam.common.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMqQueueConfig {

    @Bean
    public Queue queue(){
        return new ActiveMQQueue("queue");
    }
    
    @Bean
    public Queue queueAdd(){
        return new ActiveMQQueue("queueAdd");
    }
}