package com.blog.controllers;

import com.blog.services.IKafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/kafka")
public class KafkaController {
    @Autowired
    private IKafkaProducerService kafkaProducerService;

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage() {
        kafkaProducerService.sendMessage("Hello kafka, tôi là Nam");
        return ResponseEntity.ok("Gửi thành công");
    }
}

