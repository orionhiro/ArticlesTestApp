package com.orionhiro.ArticlesApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.orionhiro.ArticlesApp.dto.ArticleDTO;
import com.orionhiro.ArticlesApp.entity.Article;

@Mapper
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    ArticleDTO mapToArticleDTO(Article article);
}