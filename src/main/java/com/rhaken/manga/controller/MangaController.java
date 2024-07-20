package com.rhaken.manga.controller;

import com.rhaken.manga.dto.ChapterRequest;
import com.rhaken.manga.dto.MangaRequest;
import com.rhaken.manga.dto.MangaResponse;
import com.rhaken.manga.service.MangaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/manga")
@RequiredArgsConstructor
public class MangaController {

    private final MangaService service;

    @PostMapping
    public ResponseEntity<Integer> postMangaPage(@Valid @ModelAttribute MangaRequest request) {
        return ResponseEntity.ok(service.postMangaPage(request));
    }
    @GetMapping
    public ResponseEntity<List<MangaResponse>> getAllManga() {
        return ResponseEntity.ok(service.getAllManga());
    }

    @GetMapping("/{mangaId}")
    public ResponseEntity<MangaResponse> getMangaById(@PathVariable Integer mangaId) {
        return ResponseEntity.ok(service.getMangaById(mangaId));
    }

    @PatchMapping("/chapter-upload/{mangaId}")
    public ResponseEntity<Integer> postChapter(@Valid @ModelAttribute ChapterRequest request, @PathVariable Integer mangaId) throws Exception {
        return ResponseEntity.ok(service.postChapter(request, mangaId));
    }

    @DeleteMapping("/{mangaId}")
    public ResponseEntity<String> deleteMangaById(@PathVariable Integer mangaId) {
        return ResponseEntity.ok(service.deleteMangaById(mangaId));
    }

}
