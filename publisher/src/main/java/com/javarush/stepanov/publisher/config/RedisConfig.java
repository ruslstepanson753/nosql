package com.javarush.stepanov.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.connect-timeout}")
    private Long timeout;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        var lettuceClientConfiguration = LettuceClientConfiguration
                .builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .build();
        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate() {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionFactory();
        lettuceConnectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        var serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
