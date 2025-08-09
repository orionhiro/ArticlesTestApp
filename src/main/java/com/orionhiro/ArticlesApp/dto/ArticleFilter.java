package com.orionhiro.ArticlesApp.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ArticleFilter {
    private String title;
    private LocalDate date;
    private Long author_id;

    public boolean isBlank(){
        return author_id == null && date == null && title == null;
    }
}
