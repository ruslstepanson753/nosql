package com.javarush.stepanov.mvc.repository.impl;

import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.model.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepo extends JpaRepository<Notice,Long> {
}
