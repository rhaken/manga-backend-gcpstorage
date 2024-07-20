package com.rhaken.manga.mapper;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.rhaken.manga.dto.MangaResponse;
import com.rhaken.manga.models.Manga;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class MangaMapper {
    private final Storage storage;
    public MangaResponse toMangaResponse(Manga manga) {
        return new MangaResponse(
                manga.getTitle(),
                manga.getAuthor(),
                manga.getDescription(),
                manga.getThumbnail(),
                manga.getChapters(),
                manga.getGenres()
        );
    }

}
