package com.javarush.stepanov.publisher.service;

import com.javarush.stepanov.publisher.model.notice.Kafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServicePub {

    private final KafkaResponseService kafkaResponseService;

    private final String topicFromDiscussionName = "OutTopic";

    public ConsumerServicePub(KafkaResponseService kafkaResponseService) {
        this.kafkaResponseService = kafkaResponseService;
    }

    @KafkaListener(topics = topicFromDiscussionName,groupId = "notice-consumer-group")
    public void consumeResponse(ConsumerRecord<String, Kafka> record) {
        Kafka response = record.value();
        // Завершаем ожидающий запрос с полученным ответом
        kafkaResponseService.completeRequest(response);
    }
}
