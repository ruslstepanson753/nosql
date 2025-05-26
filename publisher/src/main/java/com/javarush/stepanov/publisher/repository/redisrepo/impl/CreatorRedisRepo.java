package com.javarush.stepanov.publisher.repository.redisrepo.impl;

import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.repository.redisrepo.RedisRepositoryImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CreatorRedisRepo extends RedisRepositoryImpl<Long, Creator.Out> {

    private boolean allCollectionInRedis = false;

    public CreatorRedisRepo(RedisTemplate<String, Creator.Out> redisTemplate) {
        super(redisTemplate, "creator");
    }

    public boolean isAllCollectionInRedis() {
        return allCollectionInRedis;
    }

    public void setAllCollectionInRedis(boolean allCollectionInRedis) {
        this.allCollectionInRedis = allCollectionInRedis;
    }
}
