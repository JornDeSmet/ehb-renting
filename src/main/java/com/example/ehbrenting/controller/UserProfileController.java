package com.example.ehbrenting.controller;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.service.RentalService;
import com.example.ehbrenting.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final RentalService rentalService;
    private final UserService userService;

    public UserProfileController(RentalService rentalService, UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    public String userProfile(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            List<Rental> rentals = rentalService.findRentalsByUser(user);
            model.addAttribute("rentals", rentals);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("rentals", Collections.emptyList());
        }
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateUser(@AuthenticationPrincipal User user, @RequestParam String firstName, @RequestParam String lastName) {
        if (user != null) {
            userService.updateUser(user.getId(), firstName, lastName);
        }
        return "redirect:/profile";
    }
}
