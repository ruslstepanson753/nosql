package com.javarush.stepanov.publisher.repository.redisrepo;

import com.javarush.stepanov.publisher.model.creator.Creator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CreatorRedisRepo extends RedisRepositoryImpl<Long, Creator.Out> {

    public CreatorRedisRepo(RedisTemplate<String, Creator.Out> redisTemplate) {
        super(redisTemplate, "creator");
    }
}
