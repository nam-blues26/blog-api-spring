package com.blog.services;

public interface IKafkaProducerService {
    void sendMessage(String message);
}
