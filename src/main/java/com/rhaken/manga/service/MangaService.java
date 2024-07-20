package com.rhaken.manga.service;

import com.rhaken.manga.dto.ChapterRequest;
import com.rhaken.manga.dto.MangaRequest;
import com.rhaken.manga.dto.MangaResponse;
import com.rhaken.manga.mapper.MangaMapper;
import com.rhaken.manga.models.Chapter;
import com.rhaken.manga.models.Manga;
import com.rhaken.manga.repository.MangaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class MangaService {
    private final MangaRepository repository;
    private final GcsService gcsService;
    private final MangaMapper mapper;
    public Integer postMangaPage(MangaRequest request) {
        Manga manga = Manga.builder()
                .author(request.author())
                .title(request.title())
                .description(request.description())
                .genres(request.genres())
                .chapters(new ArrayList<>())
                .build();
        repository.save(manga);
        try {
            String fileUrl = gcsService.uploadFile(request.thumbnail(), manga.getId().toString());
            manga.setThumbnail(fileUrl);
            repository.save(manga);
        }
        catch (IOException e) {
            repository.delete(manga);
        }
        return manga.getId();
    }

    public List<MangaResponse> getAllManga() {
        return repository.findAll()
                .stream()
                .map(mapper::toMangaResponse)
                .collect(Collectors.toList());
    }

    public MangaResponse getMangaById(Integer mangaId) {
        return repository.findById(mangaId)
                .map(mapper::toMangaResponse)
                .orElseThrow(() -> new EntityNotFoundException("Manga Not found"));
    }

    @Transactional
    public Integer postChapter(ChapterRequest request, Integer mangaId) throws Exception {
        //extract all file and insert it to gcp
        List<String> chapterLinks = new ArrayList<>();
        int counter = 0;
        try (ZipInputStream zis = new ZipInputStream(request.chapter().getInputStream())) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    counter += 1;
                    ByteArrayInputStream bais = new ByteArrayInputStream(zis.readAllBytes());
                    String fileUrl = gcsService.uploadFile(bais, mangaId.toString()+"_"+counter);
                    chapterLinks.add(fileUrl);
                }
                zis.closeEntry();
            }
        }
        //save the data to database
        var manga = repository.findById(mangaId).orElseThrow();
        Chapter chapter = Chapter.builder()
                .chapterNo(request.chapterNo())
                .chapterLink(chapterLinks)
                .build();
        manga.addChapter(chapter);
        repository.save(manga);
        return chapter.getId();
    }

    public String deleteMangaById(Integer mangaId) {
        if(repository.existsById(mangaId)) {
            gcsService.deleteFile(mangaId);
            repository.deleteById(mangaId);
            return "Manga berhasil dihapus";
        }
        return "Manga tidak berhasil dihapus";
    }
}
