package com.javarush.stepanov.mvc.repository.impl;

import com.javarush.stepanov.mvc.model.creator.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepo extends JpaRepository<Creator,Long> {
}
