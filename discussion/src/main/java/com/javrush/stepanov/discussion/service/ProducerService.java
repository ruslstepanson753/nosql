package com.javrush.stepanov.discussion.service;

import com.javrush.stepanov.discussion.mapper.NoticeDto;
import com.javrush.stepanov.discussion.model.Kafka;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final NoticeDto mapper;

    private final KafkaTemplate<String, Kafka> kafkaTemplate;


    @Value("${topic.name}")
    private String topicName;

    public ProducerService(NoticeDto mapper, NoticeDto mapper1, KafkaTemplate<String, Kafka> kafkaTemplate) {
        this.mapper = mapper1;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, Kafka message) {
        kafkaTemplate.send(topicName, key, message);
    }

}
