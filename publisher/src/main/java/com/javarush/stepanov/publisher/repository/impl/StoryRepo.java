package com.javarush.stepanov.publisher.repository.impl;

import com.javarush.stepanov.publisher.model.story.Story;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StoryRepo extends JpaRepository<Story,Long> {

    Optional<Object> findByTitle(String title);

    @Transactional(readOnly = true)
    boolean existsByTitle(@Size(min = 2, max = 64) String title);
}
