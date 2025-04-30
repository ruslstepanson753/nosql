-- Создание keyspace, если не существует
CREATE KEYSPACE IF NOT EXISTS testKeyspace
    WITH REPLICATION = {
        'class': 'SimpleStrategy',
        'replication_factor': 1
        };

-- Установка keyspace по умолчанию
USE testKeyspace;

-- Создание таблицы, если не существует
CREATE TABLE IF NOT EXISTS tbl_notice (
                                          country  text,
                                          id       bigint,
                                          story_id bigint,
                                          content  text,
                                          PRIMARY KEY ((country), id, story_id)
);
