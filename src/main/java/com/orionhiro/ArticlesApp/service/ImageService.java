package com.orionhiro.ArticlesApp.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;

@Service
public class ImageService {

    @Value("${app.image.bucket}")
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    @SneakyThrows
    public void uploadImage(String imagePath, InputStream content){
        Path fullImagePath = Path.of(bucket, imagePath);

        try(content){
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imagePath){
        Path fullPath = Path.of(bucket, imagePath);

        return Files.exists(fullPath) ? Optional.of(Files.readAllBytes(fullPath)) : Optional.empty();
    }

    
}
