package com.orionhiro.ArticlesApp.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.orionhiro.ArticlesApp.entity.Article;
import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.mapper.ArticleMapper;

public class ArticleMapperTest {
    @Test
    void articleMapperTest(){

        LocalDate birthday = LocalDate.of(2025, 1, 1);
        LocalDateTime user_created_at = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        User testUser = User
                    .builder()
                    .id(0L)
                    .name("Testuser")
                    .email("testUser@test.com")
                    .password("testpassword")
                    .birthday(birthday)
                    .createdAt(user_created_at)
                    .isActive(false)
                    .activationCode("testcode")
                    .build();

        LocalDateTime created_at = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        LocalDateTime updated_at = LocalDateTime.of(2025, 1, 2, 1, 1, 1);
        Article article = Article.builder()
                            .id(0L)
                            .title("Test title")
                            .author(testUser)
                            .content("Test content")
                            .createdAt(created_at)
                            .updatedAt(updated_at)
                            .image("test_image.png")
                            .url_alias("0-test-title")
                            .build();

        var result = ArticleMapper.INSTANCE.mapToArticleDTO(article);

        assertEquals(article.getId(), result.getId());
        assertEquals(article.getTitle(), result.getTitle());
        assertEquals(article.getAuthor().getId(), result.getAuthor().getId());
        assertEquals(article.getContent(), result.getContent());
        assertEquals(article.getCreatedAt(), result.getCreated_at());
        assertEquals(article.getUpdatedAt(), result.getUpdated_at());
        assertEquals(article.getImage(), result.getImage());
        assertEquals(article.getUrl_alias(), result.getUrl_alias());
    }
}
