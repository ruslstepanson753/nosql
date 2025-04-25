package com.javrush.stepanov.discussion.service;

import com.javrush.stepanov.discussion.mapper.NoticeDto;
import com.javrush.stepanov.discussion.model.Kafka;
import com.javrush.stepanov.discussion.model.Notice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceDisc {

    private final NoticeServiceDisc noticeServiceDisc;
    private final NoticeDto mapper;
    private final ProducerService producerService;

    private final String topicFromPublisherName = "InTopic";

    @Value("${topic.name}")
    private String topicName;

    public ConsumerServiceDisc(NoticeServiceDisc noticeServiceDisc, NoticeDto mapper, ProducerService producerService) {
        this.noticeServiceDisc = noticeServiceDisc;
        this.mapper = mapper;
        this.producerService = producerService;
    }

    @KafkaListener(topics = topicFromPublisherName, groupId = "notice-consumer-group")
    public void consume(ConsumerRecord<String, Kafka> record) {
        System.out.println();
        Kafka kafka = record.value();
        String method = kafka.getMethod();
        try {
            switch (method) {
                case "POST":
                    noticeServiceDisc.create(kafka);
                    break;
                case "GET":
                    Long id = kafka.getId();
                    Notice.Out noticeOut = noticeServiceDisc.get(id);
                    // Создаём объект ответа на основе полученной сущности
                    Kafka response = new Kafka();
                    response.setId(id);
                    response.setMethod(method);
                    response.setState("COMPLETED");
                    response.setNotice(noticeOut);
                    // Отправляем ответ в другой топик
                    producerService.sendMessage(String.valueOf(id), response);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            // В случае ошибки отправляем ответ со статусом ERROR
            Kafka errorResponse = new Kafka();
            Long id = kafka.getId();
            errorResponse.setId(id);
            errorResponse.setMethod(kafka.getMethod());
            errorResponse.setState("ERROR");
            producerService.sendMessage(String.valueOf(id), errorResponse);
        }

    }
}
