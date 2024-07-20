package com.rhaken.manga.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ChapterRequest(
        Integer id,
        @NotNull(message = "Chapter Number can't empty")
        double chapterNo,
        @NotNull(message = "File can't empty")
        MultipartFile chapter
) {
}
