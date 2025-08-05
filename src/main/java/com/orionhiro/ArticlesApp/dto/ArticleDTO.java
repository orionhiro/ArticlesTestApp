package com.orionhiro.ArticlesApp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private Long id;
    private String title;
    private UserDTO author;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String image;
    private String url_alias;
}
