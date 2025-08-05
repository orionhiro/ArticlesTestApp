package com.orionhiro.ArticlesApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.orionhiro.ArticlesApp.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, 
                                           QuerydslPredicateExecutor<Article>{
    List<Article> findAllByOrderByCreatedAtDesc();
}
