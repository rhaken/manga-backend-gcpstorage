package com.rhaken.manga.dto;

import com.rhaken.manga.enums.GenreEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record MangaRequest(
        @NotEmpty(message = "Title can't empty")
        String title,
        String author,
        String description,
        @NotNull(message = "Thumbnail can't empty")
        MultipartFile thumbnail,
        List<GenreEnum> genres
) {
}
