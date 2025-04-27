package com.javarush.stepanov.publisher.repository.redisrepo;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Базовая реализация CRUD-операций для Redis
 * @param <ID> тип идентификатора
 * @param <T> тип сущности
 */
public class RedisRepositoryImpl<ID, T> implements RedisRepository<ID, T> {

    protected final RedisTemplate<String, T> redisTemplate;
    private final String keyPrefix;

    public RedisRepositoryImpl(RedisTemplate<String, T> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    protected String generateKey(ID id) {
        return keyPrefix + ":" + id.toString();
    }

    @Override
    public T findById(ID id) {
        return redisTemplate.opsForValue().get(generateKey(id));
    }

    @Override
    public void save(ID id, T entity) {
        redisTemplate.opsForValue().set(generateKey(id), entity);
    }

    @Override
    public void update(ID id, T entity) {
        // В Redis операции сохранения и обновления идентичны
        save(id, entity);
    }

    @Override
    public void delete(ID id) {
        redisTemplate.delete(generateKey(id));
    }

    @Override
    public boolean exists(ID id) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(generateKey(id)));
    }

    @Override
    public void saveWithExpiration(ID id, T entity, long timeout, TimeUnit timeUnit) {
        String key = generateKey(id);
        redisTemplate.opsForValue().set(key, entity);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public List<T> findAll() {
        Set<String> keys = redisTemplate.keys(keyPrefix + ":*");
        if (keys != null && !keys.isEmpty()) {
            return redisTemplate.opsForValue().multiGet(keys);
        }
        return List.of();
    }

    // Дополнительные методы для работы с коллекциями в Redis

    public void saveToHash(String hashKey, Map<String, T> entities) {
        redisTemplate.opsForHash().putAll(keyPrefix + ":" + hashKey, entities);
    }

    public T getFromHash(String hashKey, String field) {
        return (T) redisTemplate.opsForHash().get(keyPrefix + ":" + hashKey, field);
    }

    public void addToSet(String setKey, T value) {
        redisTemplate.opsForSet().add(keyPrefix + ":" + setKey, value);
    }

    public Set<T> getSetMembers(String setKey) {
        return redisTemplate.opsForSet().members(keyPrefix + ":" + setKey);
    }

    public void addToList(String listKey, T value) {
        redisTemplate.opsForList().rightPush(keyPrefix + ":" + listKey, value);
    }

    public List<T> getListRange(String listKey, long start, long end) {
        return redisTemplate.opsForList().range(keyPrefix + ":" + listKey, start, end);
    }
}
