package com.javarush.stepanov.mvc.model.storymark;

import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.model.story.Story;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_storymark")
public class StoryMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name = "story_id")  // Внешний ключ на Story
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mark_id")   // Внешний ключ на Mark
    private Mark mark;   // Ссылка на сущность Mark, а не Long

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoryMark)) return false;
        return id != null && id.equals(((StoryMark) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}