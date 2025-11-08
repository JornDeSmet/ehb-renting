package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.RegisterDTO;
import com.example.ehbrenting.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "auth/login";
    }


    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("title", "Registreren");
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDTO registerDTO, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.RegisterUser(registerDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

}
