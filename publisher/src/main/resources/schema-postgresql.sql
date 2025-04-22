-- 1. Создаём базу данных (выполняется отдельно!)
CREATE DATABASE distcomp;

-- 2. Подключаемся к базе (если в psql)
-- \c distcomp

-- 3. Создаём схему (если ещё не создана)
CREATE SCHEMA IF NOT EXISTS public;

-- 4. Устанавливаем схему по умолчанию
SET search_path TO public;

-- 5. Создаём таблицу пользователей (создателей)
CREATE TABLE IF NOT EXISTS tbl_creator (
                                           id BIGSERIAL PRIMARY KEY,
                                           login VARCHAR(255),
                                           password VARCHAR(255),
                                           firstname VARCHAR(255),
                                           lastname VARCHAR(255)
);

-- 6. Таблица меток
CREATE TABLE IF NOT EXISTS tbl_mark (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) UNIQUE
);

-- 7. Таблица историй (без связей)
CREATE TABLE IF NOT EXISTS tbl_story (
                                         id BIGSERIAL PRIMARY KEY,
                                         creator_id BIGINT,
                                         title VARCHAR(64),
                                         content VARCHAR(2048),
                                         created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. Таблица уведомлений
CREATE TABLE IF NOT EXISTS notice (
                                      id BIGSERIAL PRIMARY KEY,
                                      story_id BIGINT,
                                      content TEXT,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. Таблица связей между историями и метками
CREATE TABLE IF NOT EXISTS tbl_storymark (
                                             id BIGSERIAL PRIMARY KEY,
                                             story_id BIGINT,
                                             mark_id BIGINT,
                                             UNIQUE(story_id, mark_id)
);