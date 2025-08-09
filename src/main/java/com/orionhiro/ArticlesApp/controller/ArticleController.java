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
import com.orionhiro.ArticlesApp.dto.UserDTO;
import com.orionhiro.ArticlesApp.service.ArticleService;
import com.orionhiro.ArticlesApp.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/articles")
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

        if(!userService.checkIsActivated(userDetails.getUsername())){
            return "redirect:/profile";
        }

        if(bindingResult.hasErrors()){
            return "createArticle";
        }
        ArticleDTO articleDTO = articleService.createArticle(createArticleDTO, userDetails.getUsername());
        return "redirect:/articles/" + articleDTO.getId() + "-" + articleDTO.getUrl_alias();
    }

    @GetMapping("/{id:\\d+}")
    public String showArticle(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        ArticleDTO articleDTO = articleService.findArticleById(id);
        UserDTO userDTO = null;
        if(userDetails != null){
            userDTO = userService.getUserByEmail(userDetails.getUsername());
        }
        model.addAttribute("articleDTO", articleDTO);
        model.addAttribute("userDTO", userDTO);

        return "showArticle";
    }

    @GetMapping("/{id:\\d+}-{alias}")
    public String showArticleAlias(
            Model model, 
            @PathVariable("id") Long id, 
            @PathVariable("alias") String alias,
            @AuthenticationPrincipal UserDetails userDetails
        ){
        ArticleDTO articleDTO = articleService.findArticleByAlias(id, alias);
        UserDTO userDTO = null;
        if(userDetails != null){
            userDTO = userService.getUserByEmail(userDetails.getUsername());
        }
        
        model.addAttribute("articleDTO", articleDTO);
        model.addAttribute("userDTO", userDTO);

        return "showArticle";
    }

    @GetMapping("/{id:\\d+}/edit")
    public String editPage(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        ArticleDTO article = articleService.findArticleById(id);
        UserDTO user = userService.getUserByEmail(userDetails.getUsername());

        if(article.getAuthor().getId() != user.getId()){
            return "redirect:/profile";
        }

        model.addAttribute("articleDTO", article);
        return "edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editArticle(
            @Valid @ModelAttribute("articleDTO") CreateArticleDTO createArticleDTO, 
            BindingResult bindingResult,
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails){
        if(bindingResult.hasErrors()){
            return "edit";
        }

        UserDTO user = userService.getUserByEmail(userDetails.getUsername());
        ArticleDTO article = articleService.findArticleById(id);
        if(user.getId() != article.getAuthor().getId()){
            return "redirect:/";
        }

        ArticleDTO articleDTO = articleService.editArticle(id, createArticleDTO);
        return "redirect:/articles/" + articleDTO.getId() + "-" + articleDTO.getUrl_alias();
    }

    @GetMapping("/{id:\\d+}/delete")
    public String deletePage(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        UserDTO user = userService.getUserByEmail(userDetails.getUsername());
        ArticleDTO article = articleService.findArticleById(id);

        if(user.getId() != article.getAuthor().getId()){
            return "redirect:/";
        }
        articleService.delete(id);
        return "redirect:/";
    }

}
