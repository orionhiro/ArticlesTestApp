package com.orionhiro.ArticlesApp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orionhiro.ArticlesApp.dto.ArticleDTO;
import com.orionhiro.ArticlesApp.dto.CreateArticleDTO;
import com.orionhiro.ArticlesApp.service.ArticleService;
import com.orionhiro.ArticlesApp.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/article")
@AllArgsConstructor
public class ArticleController {

    private UserService userService;
    private ArticleService articleService;

    @GetMapping("/create")
    public String showPageAddArticle(Model model, @AuthenticationPrincipal UserDetails userDetails){
        if(!userService.checkIsActivated(userDetails.getUsername())){
            return "redirect:/profile";
        }

        model.addAttribute("articleDTO", new CreateArticleDTO());
        return "createArticle";
    }

    @PostMapping("/create")
    public String addArticle(
            @Valid @ModelAttribute("articleDTO") CreateArticleDTO createArticleDTO, 
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails
        ){

        if(bindingResult.hasErrors()){
            return "createArticle";
        }
        ArticleDTO articleDTO = articleService.createArticle(createArticleDTO, userDetails.getUsername());
        return "redirect:/article/" + articleDTO.getId() + "-" + articleDTO.getUrl_alias();
    }

    @GetMapping("/{id:\\d+}")
    public String showArticle(Model model, @PathVariable("id") Long id){
        ArticleDTO articleDTO = articleService.findArticleById(id);
        model.addAttribute("articleDTO", articleDTO);

        return "showArticle";
    }

    @GetMapping("/{id:\\d+}-{alias}")
    public String showArticleAlias(
            Model model, 
            @PathVariable("id") Long id, 
            @PathVariable("alias") String alias
        ){
        ArticleDTO articleDTO = articleService.findArticleByAlias(id, alias);
        model.addAttribute("articleDTO", articleDTO);

        return "showArticle";
    }

}
