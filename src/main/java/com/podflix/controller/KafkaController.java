package com.podflix.controller;

import com.podflix.kafka.EventProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final EventProducer eventProducer;

    public KafkaController(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @PostMapping("/publish")
    public String publish(@RequestParam("message") String message) {
        eventProducer.sendMessage(message);
        return "Message sent to Kafka topic";
    }
}
