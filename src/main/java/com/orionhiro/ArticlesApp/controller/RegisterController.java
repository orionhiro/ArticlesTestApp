package com.orionhiro.ArticlesApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.orionhiro.ArticlesApp.dto.RegisterDTO;
import com.orionhiro.ArticlesApp.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class RegisterController {

    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerDTO") RegisterDTO registerRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "register";
        }

        userService.createUser(registerRequest).toString();
        return "redirect:/login";
    }
}
