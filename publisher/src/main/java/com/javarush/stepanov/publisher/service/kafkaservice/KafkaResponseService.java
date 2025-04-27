package com.javarush.stepanov.publisher.service.kafkaservice;

import com.javarush.stepanov.publisher.model.notice.Kafka;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KafkaResponseService {
    final Map<Long, CompletableFuture<Kafka>> pendingRequests = new ConcurrentHashMap<>();

    public CompletableFuture<Kafka> createPendingRequest(Long id) {
        CompletableFuture<Kafka> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        return future;
    }

    public void completeRequest(Kafka response) {
        CompletableFuture<Kafka> future = pendingRequests.remove(response.getId());
        if (future != null) {
            future.complete(response);
        }
    }


}
