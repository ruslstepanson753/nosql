package com.javarush.stepanov.mvc.controller;


import com.javarush.stepanov.mvc.model.creator.Creator;
import com.javarush.stepanov.mvc.model.notice.Notice;
import com.javarush.stepanov.mvc.service.NoticeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
@Slf4j
@RestController
@RequestMapping("/api/v1.0/notices")
public class NoticeController {

    private final NoticeService service;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;
    private final WebClient webClient;

    public NoticeController(NoticeService service, DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration) {
        this.service = service;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
        this.webClient = WebClient.create("http://localhost:24130");
    }

    @GetMapping("/{id}")
    public Notice.Out getNoticeById(@PathVariable Long id) {
        return webClient.get()
                .uri("/api/v1.0/notices/{id}", id)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND || status == HttpStatus.BAD_REQUEST,
                        response -> Mono.error(new RuntimeException("Error: " + response.statusCode()))
                )
                .bodyToMono(new ParameterizedTypeReference<Notice.Out>() {})
                .block(); // Block to get the result
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
    public Mono<Notice.Out> createNotice(@RequestBody @Valid Notice.In input) {
        log.info("Sending to 24130: {}", input);

        return webClient.post()
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
                });
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
