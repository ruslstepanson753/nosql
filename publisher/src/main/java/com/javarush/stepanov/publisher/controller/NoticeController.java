package com.javarush.stepanov.publisher.controller;


import com.javarush.stepanov.publisher.model.notice.Notice;
import com.javarush.stepanov.publisher.service.entityservice.NoticeService;
import com.javarush.stepanov.publisher.service.kafkaservice.ProducerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/notices")
public class NoticeController {

    private final NoticeService service;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;
    private final WebClient webClient;
    private final ProducerService kafkaProducerService;
    private final NoticeService noticeService;

    public NoticeController(NoticeService service,
                            DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration,
                            ProducerService kafkaProducerService, NoticeService noticeService) {
        this.service = service;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
        this.webClient = WebClient.create("http://localhost:24130");
        this.kafkaProducerService = kafkaProducerService;
        this.noticeService = noticeService;
    }

    @GetMapping("/{id}")
    public Notice.Out getNoticeById(@PathVariable Long id) {
        return kafkaProducerService.kafkaGet(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Notice.Out> getAllNotices() {
        return kafkaProducerService.kafkaGetAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notice.Out createNotice(@RequestBody @Valid Notice.In input) {
        log.info("Sending to 24130: {}", input);
        try{
            noticeService.chekExceptionForCreate(input);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return kafkaProducerService.kafkaPost(input);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Notice.Out  updateNotice(@RequestBody @Valid Notice.In input) {
        return kafkaProducerService.kafkaPut(input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Notice.Out deleteNotice(@PathVariable Long id) {
        try {
            return kafkaProducerService.kafkaDelete(id);
        }catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
