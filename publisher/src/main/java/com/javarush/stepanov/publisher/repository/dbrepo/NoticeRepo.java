package com.javarush.stepanov.publisher.repository.dbrepo;

import com.javarush.stepanov.publisher.model.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepo extends JpaRepository<Notice,Long> {
}
