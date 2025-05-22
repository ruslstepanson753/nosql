package com.javrush.stepanov.discussion.repo;

import com.javrush.stepanov.discussion.model.Notice;
import com.javrush.stepanov.discussion.model.NoticeKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NoticeCassandraRepository extends CassandraRepository<Notice, NoticeKey> {
    // Поиск по полю id из составного ключа (возвращает Optional)
    @Query("SELECT * FROM tbl_notice WHERE id = ?0 LIMIT 1 ALLOW FILTERING")
    Optional<Notice> findOneByKeyId(Long id);
}