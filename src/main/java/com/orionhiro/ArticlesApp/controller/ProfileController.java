package com.orionhiro.ArticlesApp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orionhiro.ArticlesApp.dto.UserDTO;
import com.orionhiro.ArticlesApp.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private UserService userService;

    @GetMapping("/")
    public String getProfile(Model model, @AuthenticationPrincipal UserDetails userDetails){
        UserDTO userDTO = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("userDTO", userDTO);
        return "profile";
    }

    @GetMapping("/{id}")
    public String getProfileById(Model model, @PathVariable("id") Long id){
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("userDTO", userDTO);
        return "profile";
    }
}
