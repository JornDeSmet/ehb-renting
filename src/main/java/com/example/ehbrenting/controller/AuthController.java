package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.user.RegisterDTO;
import com.example.ehbrenting.exceptions.EmailAlreadyExistsException;
import com.example.ehbrenting.exceptions.PasswordMismatchException;
import com.example.ehbrenting.exceptions.UsernameAlreadyExistsException;
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
        model.addAttribute("title", "Login");
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
    public String register(
            @Valid RegisterDTO registerDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(registerDTO);
        }
        catch (PasswordMismatchException e) {
            result.rejectValue(
                    "confirmPassword",
                    "error.confirmPassword",
                    e.getMessage()
            );
            return "auth/register";
        }
        catch (UsernameAlreadyExistsException e) {
            result.rejectValue(
                    "username",
                    "error.username",
                    e.getMessage()
            );
            return "auth/register";
        }
        catch (EmailAlreadyExistsException e) {
            result.rejectValue(
                    "email",
                    "error.email",
                    e.getMessage()
            );
            return "auth/register";
        }
        redirectAttributes.addFlashAttribute(
                "success",
                "Registration successful! Please login."
        );
        return "redirect:/login";
    }




}
