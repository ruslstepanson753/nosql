package com.javarush.stepanov.publisher.service.kafkaservice;

import com.javarush.stepanov.publisher.mapper.NoticeDto;
import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.model.notice.Notice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
        long id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        String method = "POST";
        String state = "PENDING";
        Notice.Out out = mapper.getOutFromIn(input);
        out.setId(id);

        Kafka kafka = getKafka(id, method, state, out);
        sendMessage(String.valueOf(id),kafka);
        return out;
    }

    public Notice.Out kafkaGet(Long id) {
        String method = "GET";
        String state = "PENDING";

        Kafka kafka = getKafka(id, method, state, null);
        return getOut(id, kafka);
    }

    public List<Notice.Out> kafkaGetAll() {
        Long id = UUID.randomUUID().getMostSignificantBits();
        String method = "GET_ALL";
        String state = "PENDING";

        Kafka kafka = getKafka(id, method, state, null);
        return getOuts(id, kafka);
    }

    public Notice.Out kafkaPut(Notice.In input) {
        Long id = input.getId();
        String method = "PUT";
        String state = "PENDING";
        Notice.Out out = mapper.getOutFromIn(input);

        Kafka kafka = getKafka(id, method, state, out);
        return getOut(id, kafka);
    }

    public Notice.Out kafkaDelete(Long id) {
        String method = "DELETE";
        String state = "PENDING";

        Kafka kafka = getKafka(id, method, state, null);
        return getOut(id, kafka);
    }

    private static Kafka getKafka(Long id, String method, String state, Notice.Out out) {
        Kafka kafka = new Kafka();
        kafka.setId(id);
        kafka.setMethod(method);
        kafka.setState(state);
        kafka.setNotice(out);
        return kafka;
    }

    private Notice.Out getOut(Long id, Kafka kafka) {
        CompletableFuture<Kafka> futureResponse = responseService.createPendingRequest(id);
        sendMessage(String.valueOf(id), kafka);
        try {
            Kafka response = futureResponse.get(1, TimeUnit.SECONDS);
            if ("ERROR".equals(response.getState())) {
                throw new RuntimeException("Ошибка при получении данных для id=" + id);
            }
            return response.getNotice();
        } catch (TimeoutException e) {
            throw new RuntimeException("Превышено время ожидания ответа");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            responseService.pendingRequests.remove(id);
        }
    }

    private List<Notice.Out> getOuts(Long id, Kafka kafka) {
        CompletableFuture<Kafka> futureResponse = responseService.createPendingRequest(id);
        sendMessage(String.valueOf(id), kafka);
        try {
            Kafka response = futureResponse.get(1, TimeUnit.SECONDS);
            if ("ERROR".equals(response.getState())) {
                throw new RuntimeException("Ошибка при получении данных для id=" + id);
            }
            return response.getNotices();
        } catch (TimeoutException e) {
            throw new RuntimeException("Превышено время ожидания ответа");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            responseService.pendingRequests.remove(id);
        }
    }
}
