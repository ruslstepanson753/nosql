package com.javrush.stepanov.discussion.model;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_notice")
public class Notice {
    @PrimaryKey
    private NoticeKey key;  // Используем составной ключ

    private String content;

    private State state;

    // Геттеры для удобства
    public String getCountry() {
        return key.getCountry();
    }

    public Long getId() {
        return key.getId();
    }

    public Long getStoryId() {
        return key.getStoryId();
    }

    // DTO классы остаются без изменений
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class In {
        @Positive Long id;
        @Positive Long storyId;
        @Size(min = 4, max = 2048) String content;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Out {
        Long id;
        Long storyId;
        String content;
    }

}
