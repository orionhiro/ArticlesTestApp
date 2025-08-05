package com.orionhiro.ArticlesApp.entity;

import java.time.LocalDateTime;

import com.orionhiro.ArticlesApp.utils.AliasUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)  
    @JoinColumn(name = "user_id")
    private User author;
    private String url_alias;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String image;

    @PrePersist
    @PreUpdate
    public void generateUrlAlias(){
        this.url_alias = AliasUtil.slugify(title);
    }
}
