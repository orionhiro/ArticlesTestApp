package com.orionhiro.ArticlesApp.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Value;

@Value
public class PageResponse<T> {
    List<T> content;
    Metadata metadata;

    @Value
    public static class Metadata{
        int page;
        int size;
        long totalPages;
    }

    public static <T> PageResponse<T> of(Page<T> page){
        var metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalPages());
        return new PageResponse<>(page.getContent(), metadata);
    }
}
