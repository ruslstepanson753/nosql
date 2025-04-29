package com.javarush.stepanov.publisher.controller;

import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.repository.dbrepo.CreatorRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.javarush.stepanov.publisher.controller.CreatorController.ENDPOINT_CREATORS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CreatorControllerTest {

    public static final Long TEST_ID = 1L;

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
                    .withExposedPorts(6379);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    CreatorRepo creatorRepo;

    // Определение контейнеров
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        // Liquibase (если включен)
        registry.add("spring.liquibase.url", postgresContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgresContainer::getUsername);
        registry.add("spring.liquibase.password", postgresContainer::getPassword);
    }

    @BeforeAll
    static void setup() {
        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port",
                REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test-key", "test-value");
        assertEquals("test-value", redisTemplate.opsForValue().get("test-key"));
    }

//    @BeforeEach
//    @Transactional
//    void initTestData() {
//        Creator creator = new Creator(1L, "testLogin", "testPass", "Test", "User");
//        creatorRepo.save(creator);
//    }

    @Test
    void findAll() {
        webTestClient
                .get()
                .uri(ENDPOINT_CREATORS)
                .header("accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Creator.In.class)
                .hasSize(1);
    }
}