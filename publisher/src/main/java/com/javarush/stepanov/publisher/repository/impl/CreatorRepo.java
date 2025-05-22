package com.javarush.stepanov.publisher.repository.impl;

import com.javarush.stepanov.publisher.model.creator.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepo extends JpaRepository<Creator,Long> {
}
