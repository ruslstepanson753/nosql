package com.javrush.stepanov.discussion.service;

import com.javrush.stepanov.discussion.model.Kafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private final String topicName = "In4";

    @KafkaListener(topics = topicName)
    public void consume(ConsumerRecord<String, Kafka> record) {
        System.out.println("!".repeat(100));
        System.out.println(record.value());
        System.out.println("!".repeat(100));
    }
}
