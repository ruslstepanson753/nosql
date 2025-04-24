package com.javarush.stepanov.publisher.repository.impl;

import com.javarush.stepanov.publisher.model.mark.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MarkRepo extends JpaRepository<Mark,Long> {
    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Mark m WHERE m.name = :name")
    boolean existsByName(@Param("name") String name);

    Mark findByName(String markName);
}
