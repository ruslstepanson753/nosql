package com.javarush.stepanov.publisher.repository.redisrepo.impl;

import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.model.mark.Mark;
import com.javarush.stepanov.publisher.repository.redisrepo.RedisRepositoryImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MarkRedisRepo extends RedisRepositoryImpl<Long, Mark.Out> {

    private boolean allCollectionInRedis = false;

    public MarkRedisRepo(RedisTemplate<String, Mark.Out> redisTemplate) {
        super(redisTemplate, "mark");
    }

    public boolean isAllCollectionInRedis() {
        return allCollectionInRedis;
    }

    public void setAllCollectionInRedis(boolean allCollectionInRedis) {
        this.allCollectionInRedis = allCollectionInRedis;
    }
}
