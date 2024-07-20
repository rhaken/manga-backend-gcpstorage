package com.rhaken.manga.models;

import com.rhaken.manga.enums.GenreEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity(name = "Manga")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Manga {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String author;
    private String description;
    private String thumbnail;
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters;
    @ElementCollection(targetClass = GenreEnum.class)
    @Enumerated(EnumType.STRING)
    private List<GenreEnum> genres;

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
        chapter.setManga(this);
    }
}
