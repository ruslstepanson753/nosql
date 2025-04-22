package com.example.discussion.controller;


import com.example.discussion.model.Notice;
import com.example.discussion.service.NoticeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1.0/notices")  // Изменил путь для соответствия запросам от 24110
public class NoticeController {

    private final NoticeService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Notice.Out> getAllNotices() {
        log.info("Получение всех уведомлений");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Notice.Out getNoticeById(@PathVariable Long id) {
        log.info("Получение уведомления с id: {}", id);
        try {
            return service.get(id);
        } catch (NoSuchElementException e) {
            log.warn("Уведомление с id {} не найдено", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Уведомление не найдено");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notice.Out createNotice(@RequestBody @Valid Notice.In input) {
        log.info("Создание нового уведомления");
        try {
            return service.create(input);
        } catch (NoSuchElementException e) {
            log.error("Ошибка создания уведомления: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Не удалось создать уведомление");
        }
    }

    @PutMapping
    public ResponseEntity<Notice.Out> updateNotice(@RequestBody @Valid Notice.In input) {
        log.info("Обновление уведомления");
        try {
            return ResponseEntity.ok(service.update(input));
        } catch (NoSuchElementException e) {
            log.warn("Уведомление для обновления не найдено");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Уведомление не найдено");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        log.info("Удаление уведомления с id: {}", id);
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            log.warn("Уведомление с id {} не найдено для удаления", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Уведомление не найдено");
        }
    }
}
