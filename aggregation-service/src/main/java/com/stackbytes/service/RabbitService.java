package com.stackbytes.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RabbitService {

    String INTEREST_KEY = "interest:";

    private final RedisTemplate<String, String> redisTemplate;

    public RabbitService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private void addTimedInterest(String interest){
        long timestamp = Instant.now().toEpochMilli();
        redisTemplate.opsForZSet().add(INTEREST_KEY, interest, timestamp);
    }

    @RabbitListener(queues = "interests")
    public void receive(String message) {
        System.out.println("Received: " + message);
        addTimedInterest(message);
    }
}
