package com.javarush.stepanov.mvc.model.notice;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long storyId;
    String content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class In{
        @Positive
        Long id;
        @Positive
        Long storyId;
        @Size(min = 4, max = 2048)
        String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Out{
        Long id;
        Long storyId;
        String content;
    }

}
