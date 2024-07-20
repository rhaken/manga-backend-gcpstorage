package com.rhaken.manga.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.rhaken.manga.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

@Service
@RequiredArgsConstructor
public class GcsService {

    private final Storage storage;
    private final Tika tika = new Tika();
    private final MangaRepository mangaRepository;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String uniqueId) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = uniqueId + "." + extension;
        String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .build();
        storage.create(blobInfo, file.getBytes());
        return fileUrl;
    }

    public String uploadFile(InputStream file, String uniqueId) throws Exception {
        String extension = MimeTypes.getDefaultMimeTypes().forName(tika.detect(file)).getExtension();
        String fileName = uniqueId + extension;
        String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .build();
        storage.create(blobInfo, file.readAllBytes());
        return fileUrl;
    }

    public void deleteFile(Integer mangaId) {
        var manga = mangaRepository.findById(mangaId).orElseThrow();
        //delete thumbnail
        String thumbnailName = extractFileName(manga.getThumbnail());
        BlobId blobThumbnail = BlobId.of(bucketName, thumbnailName);
        storage.delete(blobThumbnail);

        //delete all chapter
        manga.getChapters().forEach(chapter -> {
            chapter.getChapterLink().forEach(link -> {
                String imageName = extractFileName(link);
                BlobId blobChapter = BlobId.of(bucketName, imageName);
                storage.delete(blobChapter);
            });
        });
    }

    public String extractFileName(String link) {
        int lastSlashIndex = link.lastIndexOf('/');
        if(lastSlashIndex != -1 && lastSlashIndex < link.length()-1) {
            return link.substring(lastSlashIndex+1);
        }
        return "FileTidakDitemukan";
    }
}
