package com.javarush.stepanov.publisher.service;

import com.javarush.stepanov.publisher.mapper.NoticeDto;
import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.model.notice.Notice;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ProducerService {

    private final NoticeDto mapper;

    private final KafkaTemplate<String, Kafka> kafkaTemplate;

    private final KafkaResponseService responseService;

    @Value("${topic.name}")
    private String topicName;

    public ProducerService(NoticeDto mapper, NoticeDto mapper1, KafkaTemplate<String, Kafka> kafkaTemplate, KafkaResponseService kafkaResponseService) {
        this.mapper = mapper1;
        this.kafkaTemplate = kafkaTemplate;
        this.responseService = kafkaResponseService;
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

    public Notice.Out kafkaGet(Long id) throws Exception {
        Kafka kafka = new Kafka();
        String method = "GET";
        String state = "PENDING";
        kafka.setId(id);
        kafka.setMethod(method);
        kafka.setState(state);
        // Создаём будущий результат до отправки сообщения
        CompletableFuture<Kafka> futureResponse = responseService.createPendingRequest(id);
        // Отправляем сообщение
        sendMessage(String.valueOf(id), kafka);
        try {
            // Ждём ответ максимум 1 секунду
            Kafka response = futureResponse.get(1, TimeUnit.SECONDS);

            // Проверяем статус ответа
            if ("ERROR".equals(response.getState())) {
                throw new RuntimeException("Ошибка при получении данных для id=" + id);
            }

            // Преобразуем результат через маппер
            return mapper.fromKafka(response);
        } catch (TimeoutException e) {
            throw new RuntimeException("Превышено время ожидания ответа");
        } finally {
            // На всякий случай удаляем запрос из ожидающих
            responseService.pendingRequests.remove(id);
        }
    }
}
