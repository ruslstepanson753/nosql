package com.javarush.stepanov.publisher.repository.redisrepo.impl;

import com.javarush.stepanov.publisher.model.notice.Notice;
import com.javarush.stepanov.publisher.repository.redisrepo.RedisRepositoryImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRedisRepo extends RedisRepositoryImpl<Long, Notice.Out> {

    private boolean allCollectionInRedis = false;

    public NoticeRedisRepo(RedisTemplate<String, Notice.Out> redisTemplate) {
        super(redisTemplate, "notice");
    }

    public boolean isAllCollectionInRedis() {
        return allCollectionInRedis;
    }

    public void setAllCollectionInRedis(boolean allCollectionInRedis) {
        this.allCollectionInRedis = allCollectionInRedis;
    }
}
