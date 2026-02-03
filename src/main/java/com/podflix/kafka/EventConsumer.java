package com.podflix.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    @KafkaListener(topics = "podflix-events", groupId = "podflix-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
