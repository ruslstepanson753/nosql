package com.javarush.stepanov.mvc.repository.impl;

import com.javarush.stepanov.mvc.model.storymark.StoryMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryMarkRepo extends JpaRepository<StoryMark, Long> {

}
