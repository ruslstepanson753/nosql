package com.javarush.stepanov.publisher.repository.impl;

import com.javarush.stepanov.publisher.model.creator.Creator;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepo extends JpaRepository<Creator,Long> {

    Optional<Creator> getUserByLogin(String username);
}
