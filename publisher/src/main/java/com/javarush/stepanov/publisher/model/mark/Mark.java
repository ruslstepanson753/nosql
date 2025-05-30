package com.javarush.stepanov.publisher.model.mark;

import com.javarush.stepanov.publisher.model.storymark.StoryMark;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_mark")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    @OneToMany(mappedBy = "mark", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryMark> storys = new HashSet<>();

    public void addStorys(StoryMark storyMark){
        storys.add(storyMark);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Out implements Serializable {
        Long id;
        String name;
        Set<Long> storyIds; // ID связанных историй
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class In {
        Long id;
        String name;
        Set<Long> storyIds; // ID связанных историй
    }

    // Используем только id для equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        return id != null && id.equals(((Mark) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
