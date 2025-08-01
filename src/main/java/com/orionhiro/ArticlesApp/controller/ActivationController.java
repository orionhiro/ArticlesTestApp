package com.orionhiro.ArticlesApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orionhiro.ArticlesApp.dto.UserDTO;
import com.orionhiro.ArticlesApp.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ActivationController {

    private UserService userService;

    @GetMapping("/activate")
    public String activateAccount(@RequestParam String code){
        userService.activateAccount(code);
        return "activation";
    }
}
