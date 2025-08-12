package com.orionhiro.ArticlesApp.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
import com.orionhiro.ArticlesApp.utils.QPredicates;

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

    /**
     * Creates and saves new article
     * @param articleDTO New article info
     * @param authorEmail Author email
     * @return Article info
     */
    public ArticleDTO createArticle(CreateArticleDTO articleDTO, String authorEmail){
        if(articleDTO.getImage() != null) uploadImage(articleDTO.getImage());
        var author = userRepository.findByEmail(authorEmail).get();

        Article article = articleRepository.save(
            Article
                .builder()
                .title(articleDTO.getTitle())
                .author(author)
                .content(articleDTO.getContent())
                .image(articleDTO.getImage().getOriginalFilename().replace(" ", "_"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        return ArticleMapper.INSTANCE.mapToArticleDTO(article);
    }

    /**
     * Uploads image to the local storage
     * @param image Image content
     */
    @SneakyThrows
    private void uploadImage(MultipartFile image){
        if(!image.isEmpty()){
            imageService.uploadImage(image.getOriginalFilename().replace(" ", "_"), image.getInputStream());
        }
    }


    /**
     * Deletes image from the local storage
     * @param image Image path
     */
    private void deleteImage(String image){
        imageService.deleteImage(image);
    }


    /**
     * Returns article with the given id
     * @param id Article id
     * @return Article info
     */
    public ArticleDTO findArticleById(long id){
        return articleRepository.findById(id).map(ArticleMapper.INSTANCE::mapToArticleDTO).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article with id " + id + " not found")
        );
    }


    /**
     * Returns article with the given alias
     * @param id Article id
     * @param alias Article alias
     * @return Article info
     */
    public ArticleDTO findArticleByAlias(long id, String alias){
        return articleRepository
                .findById(id)
                .filter(article -> article.getUrl_alias().equals(alias))
                .map(ArticleMapper.INSTANCE::mapToArticleDTO)
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article with id " + id + " not found")
                );
    }

    /**
     * Returns list of the all articles
     * @return List of articles
     */
    public List<ArticleDTO> findAll(){
        return articleRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ArticleMapper.INSTANCE::mapToArticleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns page of the filtered articles
     * @param filter Article filter
     * @param pageable Page info
     * @return Page of the filetered articles
     */
    public Page<ArticleDTO> findAll(ArticleFilter filter, Pageable pageable){
        LocalDateTime dateTime = null;
        if(filter.getDate() == null){
            dateTime = null;
        } else{
            dateTime = LocalDateTime.of(filter.getDate(), LocalTime.MIN);
        }

        var predicate = QPredicates.builder()
                            .add(filter.getAuthor_id(), QArticle.article.author.id::eq)
                            .add(filter.getTitle(), QArticle.article.title::containsIgnoreCase)
                            .add(dateTime, QArticle.article.createdAt::before)
                            .build();

        return articleRepository.findAll(predicate, pageable)
                .map(ArticleMapper.INSTANCE::mapToArticleDTO);
    }

    /**
     * Modifies article info using new article info
     * @param id Article id
     * @param editArticleDTO New article info
     * @return Article info
     */
    public ArticleDTO editArticle(long id, CreateArticleDTO editArticleDTO){
        if(editArticleDTO.getImage() != null) uploadImage(editArticleDTO.getImage());

        return articleRepository.findById(id)
            .map(entity -> {

                if(!entity.getImage().isEmpty()) deleteImage(entity.getImage());

                entity.setTitle(editArticleDTO.getTitle());
                entity.setContent(editArticleDTO.getContent());
                entity.setImage(editArticleDTO.getImage() != null ? editArticleDTO.getImage().getOriginalFilename() : null);
                entity.setUpdatedAt(LocalDateTime.now());

                return entity;

            })
            .map(articleRepository::saveAndFlush)
            .map(ArticleMapper.INSTANCE::mapToArticleDTO).get();
    }

    /**
     * Deletes an article with the given id
     * @param id Article id
     * @return Status
     */
    public boolean delete(Long id){
        return articleRepository.findById(id)
                .map(
                    entity -> {
                        articleRepository.delete(entity);
                        articleRepository.flush();
                        return true;
                    }
                ).orElse(false);
    }
}
