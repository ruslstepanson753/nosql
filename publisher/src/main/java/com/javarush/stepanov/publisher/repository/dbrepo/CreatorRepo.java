package com.javarush.stepanov.publisher.repository.dbrepo;

import com.javarush.stepanov.publisher.model.creator.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface CreatorRepo extends JpaRepository<Creator,Long> {

    Optional<Creator> getUserByLogin(String username);

    @Transactional
   Creator save(Creator creator);
}
