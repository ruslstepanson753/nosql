-- Создаём keyspace (если не существует)
CREATE KEYSPACE IF NOT EXISTS keyspace_name
    WITH replication = {
        'class': 'SimpleStrategy',
        'replication_factor': 1
        };

-- Таблица для заметок (с проверкой на существование)
CREATE TABLE IF NOT EXISTS keyspace_name.tbl_notice (
                                                        country text,
                                                        id bigint,
                                                        story_id bigint,
                                                        content text,
                                                        PRIMARY KEY ((country), id, story_id)
) WITH CLUSTERING ORDER BY (id ASC, story_id ASC);

-- Таблица-счётчик (только для операций COUNTER)
CREATE TABLE IF NOT EXISTS keyspace_name.counters (
                                                      table_name text PRIMARY KEY,
                                                      next_id counter
);

-- Инициализация счётчика (если ещё не существует)
-- В Cassandra нельзя напрямую вставить значение счётчика, только инкрементить
-- Поэтому сначала делаем инкремент, затем проверяем и при необходимости сбрасываем
BEGIN BATCH
UPDATE keyspace_name.counters SET next_id = next_id + 0
WHERE table_name = 'tbl_notice';

APPLY BATCH;

-- Проверяем и сбрасываем если нужно (это нужно выполнить отдельно после инициализации)
-- SELECT next_id FROM keyspace_name.counters WHERE table_name = 'tbl_notice';
-- Если значение NULL или нежелательное, можно сделать:
-- UPDATE keyspace_name.counters SET next_id = next_id - [текущее_значение] 
-- WHERE table_name = 'tbl_notice';