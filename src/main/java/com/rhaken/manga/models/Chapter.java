package com.rhaken.manga.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity(name = "chapter")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue
    private Integer id;
    private double chapterNo;
    @ElementCollection(targetClass = String.class)
    private List<String> chapterLink;
    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;
}
