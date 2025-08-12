package com.orionhiro.ArticlesApp.unit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.orionhiro.ArticlesApp.dto.CreateArticleDTO;
import com.orionhiro.ArticlesApp.entity.Article;
import com.orionhiro.ArticlesApp.entity.User;
import com.orionhiro.ArticlesApp.repository.ArticleRepository;
import com.orionhiro.ArticlesApp.repository.UserRepository;
import com.orionhiro.ArticlesApp.service.ArticleService;
import com.orionhiro.ArticlesApp.service.ImageService;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    
    @InjectMocks
    private ArticleService articleService;

    private Article testArticle;
    private User testUser;

    private LocalDate birthday;
    private LocalDateTime created_at;

    private CreateArticleDTO articleDTO;

    @BeforeEach
    void setup(){

        birthday = LocalDate.of(2025, 1, 1);
        created_at = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        testUser = User
                    .builder()
                    .id(0L)
                    .name("Testuser")
                    .email("testUser@test.com")
                    .password("testpassword")
                    .birthday(birthday)
                    .createdAt(created_at)
                    .isActive(false)
                    .activationCode("testcode")
                    .build();

        testArticle = Article
                        .builder()
                        .id(0L)
                        .author(testUser)
                        .content("Test content")
                        .createdAt(created_at)
                        .updatedAt(created_at)
                        .title("Test title")
                        .image("test_image.png")
                        .url_alias("test-title")
                        .build();

        articleDTO = CreateArticleDTO
                        .builder()
                        .title("New test title")
                        .content("New test content")
                        .image(new MockMultipartFile("test_image.png", new byte[]{1}))
                        .build();
    }
    
    @Test
    void deleteTest(){
        when(articleRepository.findById(testArticle.getId())).thenReturn(Optional.of(testArticle));
        doNothing().when(articleRepository).delete(testArticle);
        doNothing().when(articleRepository).flush();

        var result = articleService.delete(testArticle.getId());

        assertEquals(true, result);
        verify(articleRepository, times(1)).delete(testArticle);
        verify(articleRepository, times(1)).flush();

    }

    @Test
    void deleteNotFoundTest(){
        when(articleRepository.findById(-1000L)).thenReturn(Optional.empty());

        var result = articleService.delete(-1000L);

        assertEquals(false, result);
        verify(articleRepository, times(0)).delete(testArticle);
        verify(articleRepository, times(0)).flush();

    }

    @Test
    void editArticleTest(){
        doNothing().when(imageService).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));
        doNothing().when(imageService).deleteImage(Mockito.anyString());
        when(articleRepository.saveAndFlush(Mockito.any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(articleRepository.findById(testArticle.getId())).thenReturn(Optional.of(testArticle));
        
        var result = articleService.editArticle(testArticle.getId(), articleDTO);

        assertNotNull(articleDTO);
        assertEquals(articleDTO.getTitle(), testArticle.getTitle());
        assertEquals(articleDTO.getContent(), testArticle.getContent());
        assertEquals(articleDTO.getImage().getOriginalFilename(), testArticle.getImage());

        verify(imageService, times(1)).deleteImage(Mockito.anyString());
        verify(imageService, times(1)).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));

        verify(articleRepository, times(1)).saveAndFlush(Mockito.any(Article.class));

    }

    @Test
    void editArticleNotFoundTest(){
        when(articleRepository.findById(-1000L)).thenReturn(Optional.empty());

        var result = articleService.editArticle(-1000L, articleDTO);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllTest(){
        when(articleRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(testArticle));

        var result = articleService.findAll();

        assertEquals(1, result.size());
        
    }

    @Test
    void findArticleByAliasTest(){
        when(articleRepository.findById(testArticle.getId())).thenReturn(Optional.of(testArticle));

        var result = articleService.findArticleByAlias(testArticle.getId(), "test-title");

        assertEquals(testArticle.getId(), result.getId());
        assertEquals("test-title", result.getUrl_alias());

    }

    @Test
    void findArticleByAliasIncorrectTest(){
        when(articleRepository.findById(testArticle.getId())).thenReturn(Optional.of(testArticle));

        assertThrows(ResponseStatusException.class, () -> articleService.findArticleByAlias(testArticle.getId(), "test-title-1"));

    }


    @Test
    void findArticleByAliasFullIncorrectTest(){
        when(articleRepository.findById(-1000L)).thenReturn(Optional.of(testArticle));

        assertThrows(ResponseStatusException.class, () -> articleService.findArticleByAlias(-1000L, "test-title-1"));

    }

    @Test
    void findByIdTest(){
        when(articleRepository.findById(testArticle.getId())).thenReturn(Optional.of(testArticle));

        var result = articleService.findArticleById(testArticle.getId());

        assertEquals(testArticle.getId(), result.getId());
        assertEquals(testArticle.getTitle(), result.getTitle());
    }

    @Test
    void findByIdNotFoundTest(){
        when(articleRepository.findById(-1000L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> articleService.findArticleById(-1000L));
    }

    @Test
    void uploadImageTest(){
        doNothing().when(imageService).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));

        articleService.uploadImage(new MockMultipartFile("test_image.png", new byte[]{1}));

        verify(imageService, times(1)).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));

    }

    @Test
    void uploadEmptyImageTest(){
        articleService.uploadImage(new MockMultipartFile("test_image.png", new byte[0]));

        verify(imageService, times(0)).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));
    }

    @Test
    void createArticleTest(){
        doNothing().when(imageService).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));
        when(articleRepository.save(Mockito.any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(testUser));
        

        var result = articleService.createArticle(articleDTO, testUser.getEmail());

        assertEquals(testUser.getEmail(), result.getAuthor().getEmail());
        assertEquals(articleDTO.getTitle(), result.getTitle());
        assertEquals(articleDTO.getContent(), result.getContent());
        assertNotNull(result.getCreated_at());
        assertNotNull(result.getUpdated_at());

    }

    @Test
    void createArticleNotFoundTest(){
        doNothing().when(imageService).uploadImage(Mockito.anyString(), Mockito.any(InputStream.class));
        when(userRepository.findByEmail("nonExistMail@test.com")).thenReturn(Optional.empty());
        

        assertThrows(ResponseStatusException.class, () -> articleService.createArticle(articleDTO, "nonExistMail@test.com"));

    }

}
