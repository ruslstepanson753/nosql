package com.example.discussion.service;

import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {

    private final CassandraOperations cassandraOperations;

    public IdGeneratorService(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
    }

    public Long getNextId() {
        String tableName = "tbl_notice";
        // Создаем параметризованный запрос для обновления счетчика
        SimpleStatement updateStatement = SimpleStatement.builder(
                        "UPDATE counters SET next_id = next_id + 1 WHERE table_name = ?")
                .addPositionalValue(tableName)
                .build();

        // Выполняем запрос обновления
        cassandraOperations.execute(updateStatement);

        // Создаем параметризованный запрос для получения нового значения
        SimpleStatement selectStatement = SimpleStatement.builder(
                        "SELECT next_id FROM counters WHERE table_name = ?")
                .addPositionalValue(tableName)
                .build();

        // Получаем результат
        Row row = cassandraOperations.selectOne(selectStatement, Row.class);

        return row != null ? row.getLong("next_id") : null;
    }
}
