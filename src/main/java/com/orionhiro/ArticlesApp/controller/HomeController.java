package com.orionhiro.ArticlesApp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.orionhiro.ArticlesApp.dto.ArticleDTO;
import com.orionhiro.ArticlesApp.dto.ArticleFilter;
import com.orionhiro.ArticlesApp.dto.PageResponse;
import com.orionhiro.ArticlesApp.service.ArticleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
public class HomeController {

    private ArticleService articleService;

    @GetMapping("/")
    public String showHomePage(Model model, @RequestParam(value = "page", required = false) Integer pageNumber){
        Pageable pageable = PageRequest.of(pageNumber != null ? pageNumber - 1 : 0, 9, Sort.by("createdAt").descending());
        Page<ArticleDTO> page = articleService.findAll(new ArticleFilter(), pageable);
        
        if(pageNumber != null && pageNumber > page.getTotalPages()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("articleList", PageResponse.of(page));
        return "home";
    }
}
