package com.orionhiro.ArticlesApp.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateArticleDTO {
    @NotBlank(message = "Название не должно быть пустым")
    @Size(max = 100, message = "Название не должно быть слишком длинным")
    private String title;
    @NotBlank(message = "Содержимое не должно быть пустым")
    private String content;
    private MultipartFile image;
}
