package com.javarush.stepanov.publisher.service;

import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.model.notice.Notice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final KafkaTemplate<String, Kafka> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, Kafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${topic.name}")
    private String topicName;

    public void sendMessage(String key, Kafka message) {
        kafkaTemplate.send(topicName, key, message);
    }

    public Notice.Out kafkaPost(Notice.In input) {
        return null;
    }
}
