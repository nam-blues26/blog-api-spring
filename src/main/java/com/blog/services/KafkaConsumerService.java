package com.blog.services;

import com.blog.responses.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@KafkaListener(topics = "all_blogs", groupId = "my_group")
public class KafkaConsumerService {

    @Autowired
    private IPostRedisService postRedisService;
    @Autowired
    private IPostService postService;
    @KafkaHandler
    public void consumeMessage(String message) throws JsonProcessingException {
        // Xử lý dữ liệu từ Kafka, ở đây chúng ta chỉ in ra console
        List<PostResponse> updatedPosts = postService.getPosts();
        postRedisService.savePostsRedis(updatedPosts);
        System.out.println("Received message: " + message);
    }
}

