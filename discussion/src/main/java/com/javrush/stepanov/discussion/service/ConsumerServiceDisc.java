package com.javrush.stepanov.discussion.service;

import com.javrush.stepanov.discussion.mapper.NoticeDto;
import com.javrush.stepanov.discussion.model.Kafka;
import com.javrush.stepanov.discussion.model.Notice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceDisc {

    private final NoticeServiceDisc noticeServiceDisc;
    private final NoticeDto mapper;

    private final String topicName = "InTopic";

    public ConsumerServiceDisc(NoticeServiceDisc noticeServiceDisc, NoticeDto mapper) {
        this.noticeServiceDisc = noticeServiceDisc;
        this.mapper = mapper;
    }

    @KafkaListener(topics = topicName)
    public void consume(ConsumerRecord<String, Kafka> record) {
        System.out.println();
        Kafka kafka = record.value();
        String method = kafka.getMethod();
        switch (method) {
            case "POST":
                noticeServiceDisc.create(kafka);
                break;
            default: break;
        }

    }
}
