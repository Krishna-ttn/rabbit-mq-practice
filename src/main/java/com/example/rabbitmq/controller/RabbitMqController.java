package com.example.rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RabbitMqController {

    private final RabbitMQProducer rabbitMQServiceProducer;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam String message, @RequestParam String email) {
        rabbitMQServiceProducer.sendMessage(message, email);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getMessage(@RequestParam String email) {
        return ResponseEntity.ok(redisTemplate.opsForValue().get(email));
    }
}

