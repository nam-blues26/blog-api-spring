package com.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements IKafkaProducerService{

    private static final String TOPIC = "all_blogs";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void sendMessage(String message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
}
