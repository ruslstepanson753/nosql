package com.javarush.stepanov.publisher.service;

import com.javarush.stepanov.publisher.mapper.NoticeDto;
import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.model.notice.Notice;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public Notice.Out kafkaPost(Notice.In input) {
        Kafka kafka = mapper.kafkaFromIn(input);
        Long id = UUID.randomUUID().getMostSignificantBits();
        String method = "POST";
        String state = "PENDING";
        kafka.setId(id);
        kafka.setMethod(method);
        kafka.setState(state);
        sendMessage(String.valueOf(id),kafka);
        return mapper.fromKafka(kafka);
    }
}
