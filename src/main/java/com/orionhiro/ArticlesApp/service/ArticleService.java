package com.orionhiro.ArticlesApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.orionhiro.ArticlesApp.dto.ArticleDTO;
import com.orionhiro.ArticlesApp.dto.ArticleFilter;
import com.orionhiro.ArticlesApp.dto.CreateArticleDTO;
import com.orionhiro.ArticlesApp.entity.Article;
import com.orionhiro.ArticlesApp.entity.QArticle;
import com.orionhiro.ArticlesApp.mapper.ArticleMapper;
import com.orionhiro.ArticlesApp.repository.ArticleRepository;
import com.orionhiro.ArticlesApp.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ArticleService {

    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private ImageService imageService;

    public ArticleDTO createArticle(CreateArticleDTO articleDTO, String authorEmail){
        uploadImage(articleDTO.getImage());
        var author = userRepository.findByEmail(authorEmail).get();

        Article article = articleRepository.save(
            Article
                .builder()
                .title(articleDTO.getTitle())
                .author(author)
                .content(articleDTO.getContent())
                .image(articleDTO.getImage().getOriginalFilename())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        return ArticleMapper.INSTANCE.mapToArticleDTO(article);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image){
        if(!image.isEmpty()){
            imageService.uploadImage(image.getOriginalFilename(), image.getInputStream());
        }
    }

    public ArticleDTO findArticleById(long id){
        return articleRepository.findById(id).map(ArticleMapper.INSTANCE::mapToArticleDTO).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article with id " + id + " not found")
        );
    }

    public ArticleDTO findArticleByAlias(long id, String alias){
        return articleRepository
                .findById(id)
                .filter(article -> article.getUrl_alias().equals(alias))
                .map(ArticleMapper.INSTANCE::mapToArticleDTO)
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article with id " + id + " not found")
                );
    }

    public List<ArticleDTO> findAll(){
        return articleRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ArticleMapper.INSTANCE::mapToArticleDTO)
                .collect(Collectors.toList());
    }

    public Page<ArticleDTO> findAll(ArticleFilter filter, Pageable pageable){
        return articleRepository.findAll(QArticle.article.title.isNotEmpty(), pageable)
                .map(ArticleMapper.INSTANCE::mapToArticleDTO);
    }
}
