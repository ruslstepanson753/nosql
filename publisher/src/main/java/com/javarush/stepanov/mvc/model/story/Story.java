package com.javarush.stepanov.mvc.model.story;

import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.model.storymark.StoryMark;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_story")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long creatorId;
    String title;
    String content;
    LocalDateTime created;
    LocalDateTime modified;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryMark> marks = new HashSet<>();

    public void addMarks(StoryMark storyMark){
        marks.add(storyMark);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class In {
        @Positive
        Long id;
        @Positive
        Long creatorId;
        @Size(min = 2, max = 64)
        String title;
        @Size(min = 4, max = 2048)
        String content;
        LocalDateTime created;
        LocalDateTime modified;
        Set<String> marks; // Имена меток
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Out {
        Long id;
        Long creatorId;
        String title;
        String content;
        LocalDateTime created;
        LocalDateTime modified;
        Set<String> marks; // Имена связанных меток
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Story)) return false;
        return id != null && id.equals(((Story) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
