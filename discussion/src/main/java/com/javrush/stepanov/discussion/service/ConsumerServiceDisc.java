package com.javrush.stepanov.discussion.service;

import com.javrush.stepanov.discussion.mapper.NoticeDto;
import com.javrush.stepanov.discussion.model.Kafka;
import com.javrush.stepanov.discussion.model.Notice;
import com.javrush.stepanov.discussion.model.NoticeKey;
import com.javrush.stepanov.discussion.repo.NoticeCassandraRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerServiceDisc {

    private final NoticeServiceDisc noticeServiceDisc;
    private final NoticeCassandraRepository repository;
    private final NoticeDto mapper;
    private final ProducerService producerService;

    private final String topicFromPublisherName = "InTopic";

    @Value("${topic.name}")
    private String topicName;

    public ConsumerServiceDisc(NoticeServiceDisc noticeServiceDisc, NoticeCassandraRepository repository, NoticeDto mapper, ProducerService producerService) {
        this.noticeServiceDisc = noticeServiceDisc;
        this.repository = repository;
        this.mapper = mapper;
        this.producerService = producerService;
    }

    @KafkaListener(topics = topicFromPublisherName, groupId = "notice-consumer-group")
    public void consume(ConsumerRecord<String, Kafka> record) {
        Kafka kafka = record.value();
        String method = kafka.getMethod();
        Long id = kafka.getId();
        try {
            switch (method) {
                case "POST":
                    noticeServiceDisc.create(kafka);
                    break;
                case "GET":
                    get(id, method);
                    break;
                case "GET_ALL":
                    getAll(id, method);
                    break;
                case "PUT":
                    put(id, kafka);
                    break;
                case "DELETE":
                    delete(id, kafka);
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            sendError(id, kafka);
        }

    }

    private void get(Long id, String method) {
        Notice.Out noticeOut = noticeServiceDisc.get(id);
        Kafka response = createResponse(id, method, noticeOut);
        producerService.sendMessage(String.valueOf(id), response);
    }

    private void getAll(Long id, String method) {
        List<Notice.Out> noticeList = noticeServiceDisc.getAll();
        Kafka responseAll = new Kafka();
        responseAll.setId(id);
        responseAll.setMethod(method);
        responseAll.setState("COMPLETED");
        responseAll.setNotices(noticeList);
        producerService.sendMessage(String.valueOf(id), responseAll);
    }

    private void put(Long id, Kafka kafka) {
        Notice.Out noticeOut = kafka.getNotice();
        Notice.In noticeIn = mapper.toInFromOut(noticeOut);
        Notice.Out resultNotice = noticeServiceDisc.update(noticeIn);
        Kafka putResposne = createResponse(id, "PUT", resultNotice);
        // Отправляем ответ в другой топик
        producerService.sendMessage(String.valueOf(id), putResposne);
    }

    private void delete(Long id, Kafka kafka) throws ChangeSetPersister.NotFoundException {
        repository.findOneByKeyId(id).orElseThrow(
                () -> {
                    kafka.setState("NOT_FOUND");
                    producerService.sendMessage(String.valueOf(id), kafka);
                    return new ChangeSetPersister.NotFoundException(); // or any other exception
                }
        );
        noticeServiceDisc.delete(id);
        producerService.sendMessage(String.valueOf(id), kafka);
    }

    private static Kafka createResponse(Long id, String method, Notice.Out noticeOut) {
        Kafka response = new Kafka();
        response.setId(id);
        response.setMethod(method);
        response.setState("COMPLETED");
        response.setNotice(noticeOut);
        return response;
    }

    private void sendError(Long id, Kafka kafka) {
        // В случае ошибки отправляем ответ со статусом ERROR
        Kafka errorResponse = new Kafka();
        errorResponse.setId(id);
        errorResponse.setMethod(kafka.getMethod());
        errorResponse.setState("ERROR");
        producerService.sendMessage(String.valueOf(id), errorResponse);
    }
}
