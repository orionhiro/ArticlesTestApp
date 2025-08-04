package com.orionhiro.ArticlesApp.controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.orionhiro.ArticlesApp.service.ImageService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) throws IOException{
        Optional<byte[]> image = imageService.get(filename);
        if(image.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Path imagePath = Paths.get(imageService.getBucket()).resolve(filename);
        String contentType = Files.probeContentType(imagePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(image.get());

    }
}
