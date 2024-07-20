package com.rhaken.manga.dto;

import com.rhaken.manga.enums.GenreEnum;
import com.rhaken.manga.models.Chapter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record MangaResponse(
        String title,
        String author,
        String description,
        String thumbnail,
        List<Chapter> chapters,
        List<GenreEnum> genres
) {
}
