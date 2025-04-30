package com.javarush.stepanov.publisher.controller;

import com.javarush.stepanov.publisher.TestPreparatory;
import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.repository.dbrepo.CreatorRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.javarush.stepanov.publisher.controller.CreatorController.ENDPOINT_CREATORS;

public class CreatorTestPreparatory extends TestPreparatory {

    @Autowired
    CreatorRepo creatorRepo;

    @BeforeEach
    @Transactional
    void initTestData() {
        Creator creator1 = new Creator(1L, "testLogin1", "testPass1", "Test1", "User1");
        Creator creator2 = new Creator(2L, "testLogin2", "testPass2", "Test2", "User2");
        Creator creator3 = new Creator(3L, "testLogin3", "testPass3", "Test3", "User3");
        creatorRepo.save(creator1);
        creatorRepo.save(creator2);
        creatorRepo.save(creator3);
    }

    @Test
    void findAll() {
        webTestClient
                .get()
                .uri(ENDPOINT_CREATORS)
                .header("accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Creator.In.class)
                .hasSize(3);
    }
}
