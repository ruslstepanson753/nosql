package com.javarush.stepanov.publisher.repository.impl;

import com.javarush.stepanov.publisher.model.storymark.StoryMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryMarkRepo extends JpaRepository<StoryMark, Long> {

}
