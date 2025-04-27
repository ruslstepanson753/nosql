package com.javarush.stepanov.publisher.service.kafkaservice;

import com.javarush.stepanov.publisher.mapper.NoticeDto;
import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.model.notice.Notice;
import com.javarush.stepanov.publisher.repository.redisrepo.impl.NoticeRedisRepo;
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

    private final NoticeRedisRepo redisRepo;

    @Value("${topic.name}")
    private String topicName;

    public ProducerService(NoticeDto mapper,
                           KafkaTemplate<String, Kafka> kafkaTemplate,
                           KafkaResponseService kafkaResponseService,
                           NoticeRedisRepo redisRepo) {
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
        this.responseService = kafkaResponseService;
        this.redisRepo = redisRepo;
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
        redisRepo.save(out.getId(), out);
        return out;
    }

    public Notice.Out kafkaGet(Long id) {
        if (redisRepo.exists(id)) {
            return redisRepo.findById(id);
        }
        String method = "GET";
        String state = "PENDING";

        Kafka kafka = getKafka(id, method, state, null);
        Notice.Out result = getOut(id, kafka);
        redisRepo.save(result.getId(), result);
        return result;
    }

    public List<Notice.Out> kafkaGetAll() {
        Long id = UUID.randomUUID().getMostSignificantBits();
        String method = "GET_ALL";
        String state = "PENDING";

        Kafka kafka = getKafka(id, method, state, null);
        List<Notice.Out> result = getOuts(id, kafka);
        result.forEach(r -> redisRepo.save(r.getId(), r));
        redisRepo.setAllCollectionInRedis(true);
        return result;
    }

    public Notice.Out kafkaPut(Notice.In input) {
        Long id = input.getId();
        String method = "PUT";
        String state = "PENDING";
        Notice.Out out = mapper.getOutFromIn(input);

        Kafka kafka = getKafka(id, method, state, out);
        Notice.Out result = getOut(id, kafka);
        redisRepo.save(result.getId(), result);
        return result;
    }

    public Notice.Out kafkaDelete(Long id) {
        String method = "DELETE";
        String state = "PENDING";

        Kafka kafka = getKafka(id, method, state, null);
        Notice.Out result = getOut(id, kafka);
        redisRepo.delete(id);
        return result;
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
