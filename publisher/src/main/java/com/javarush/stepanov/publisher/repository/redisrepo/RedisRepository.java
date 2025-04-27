package com.javarush.stepanov.publisher.repository.redisrepo;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Универсальный интерфейс для CRUD операций с Redis
 * @param <ID> тип идентификатора
 * @param <T> тип сущности
 */
public interface RedisRepository<ID, T> {
    T findById(ID id);
    void save(ID id, T entity);
    void update(ID id, T entity);
    void delete(ID id);
    boolean exists(ID id);
    void saveWithExpiration(ID id, T entity, long timeout, TimeUnit timeUnit);
    List<T> findAll(String pattern);
}

