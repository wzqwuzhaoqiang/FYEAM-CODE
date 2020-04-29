package com.fuyaogroup.eam.common.activemq;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/activemq")
public class ActiveMqClient {

    @Autowired
    private JmsTemplate jmsTemplate;

//    @Autowired
//    private Topic topic;

    @Autowired
    private Queue queue;
    @Autowired
    private Queue queueAdd;

    @RequestMapping("/send")
    public void send(){
//        jmsTemplate.convertAndSend(this.topic,"发送的topic数据!");
    	System.out.print("aaaaaaaaaa");
        jmsTemplate.convertAndSend(this.queue,"发送的queueAdd数据!");
        jmsTemplate.convertAndSend("queueAdd","发送的queue数据!");
    }
}