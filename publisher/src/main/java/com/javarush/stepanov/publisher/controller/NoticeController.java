package com.javarush.stepanov.publisher.controller;


import com.javarush.stepanov.publisher.model.notice.Notice;
import com.javarush.stepanov.publisher.service.NoticeService;
import com.javarush.stepanov.publisher.service.ProducerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/notices")
public class NoticeController {

    private final NoticeService service;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;
    private final WebClient webClient;
    private final ProducerService kafkaProducerService;

    public NoticeController(NoticeService service,
                            DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration,
                            ProducerService kafkaProducerService) {
        this.service = service;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
        this.webClient = WebClient.create("http://localhost:24130");
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/{id}")
    public Notice.Out getNoticeById(@PathVariable Long id) throws Exception {
        return kafkaProducerService.kafkaGet(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<Notice.Out>> getAllNotices() {
        return webClient.get()
                .uri("/api/v1.0/notices")
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND || status == HttpStatus.BAD_REQUEST,
                        response -> Mono.error(new RuntimeException("Error: " + response.statusCode()))
                )
                .bodyToMono(new ParameterizedTypeReference<List<Notice.Out>>() {});
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notice.Out createNotice(@RequestBody @Valid Notice.In input) {
        log.info("Sending to 24130: {}", input);
        return kafkaProducerService.kafkaPost(input);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Notice.Out  updateNotice(@RequestBody @Valid Notice.In input) {
        return webClient.put()
                .uri("/api/v1.0/notices")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Notice.Out.class)
                .onErrorMap(e -> {
                    log.error("Forwarding error", e);
                    return new ResponseStatusException(
                            HttpStatus.BAD_GATEWAY,
                            "Remote service error: " + e.getMessage()
                    );
                }).block();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteNotice(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
