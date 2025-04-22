package com.example.discussion.repo;

import com.example.discussion.model.Notice;
import com.example.discussion.model.NoticeKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NoticeRepo extends CassandraRepository<Notice, NoticeKey> {
    // Поиск по полю id из составного ключа (возвращает Optional)
    @Query("SELECT * FROM tbl_notice WHERE id = ?0 LIMIT 1 ALLOW FILTERING")
    Optional<Notice> findOneByKeyId(Long id);
}