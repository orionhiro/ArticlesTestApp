package com.orionhiro.ArticlesApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orionhiro.ArticlesApp.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{
    
}
