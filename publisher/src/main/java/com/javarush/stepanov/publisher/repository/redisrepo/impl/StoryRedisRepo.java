package com.javarush.stepanov.publisher.repository.redisrepo.impl;

import com.javarush.stepanov.publisher.model.story.Story;
import com.javarush.stepanov.publisher.repository.redisrepo.RedisRepositoryImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StoryRedisRepo extends RedisRepositoryImpl<Long, Story.Out> {

    private boolean allCollectionInRedis = false;

    public StoryRedisRepo(RedisTemplate<String, Story.Out> redisTemplate) {
        super(redisTemplate, "story");
    }

    public boolean isAllCollectionInRedis() {
        return allCollectionInRedis;
    }

    public void setAllCollectionInRedis(boolean allCollectionInRedis) {
        this.allCollectionInRedis = allCollectionInRedis;
    }
}
