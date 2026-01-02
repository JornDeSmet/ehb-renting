package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.user.ChangePasswordDTO;
import com.example.ehbrenting.dto.user.UserProfileDTO;
import com.example.ehbrenting.exceptions.InvalidPasswordException;
import com.example.ehbrenting.exceptions.PasswordMismatchException;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.service.RentalService;
import com.example.ehbrenting.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final RentalService rentalService;
    private final UserService userService;

    public UserProfileController(RentalService rentalService,
                                 UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    public String userProfile(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("profile", userService.getProfile(user));
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        model.addAttribute("rentals", rentalService.findRentalsByUser(user));
        return "user/profile";
    }

    // ===== PROFILE UPDATE =====
    @PostMapping("/update")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @Valid UserProfileDTO profileDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            model.addAttribute("rentals", rentalService.findRentalsByUser(user));
            return "user/profile";
        }

        userService.updateProfile(user, profileDTO);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Gegevens succesvol aangepast"
        );

        return "redirect:/profile";
    }


    // ===== PASSWORD CHANGE =====
    @PostMapping("/password")
    public String changePassword(
            @AuthenticationPrincipal User user,
            @Valid ChangePasswordDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("profile", userService.getProfile(user));
            model.addAttribute("rentals", rentalService.findRentalsByUser(user));
            return "user/profile";
        }

        try {
            userService.changePassword(user, dto);
        } catch (InvalidPasswordException e) {
            result.rejectValue("currentPassword", null, e.getMessage());
        } catch (PasswordMismatchException e) {
            result.rejectValue("confirmNewPassword", null, e.getMessage());
        }

        if (result.hasErrors()) {
            model.addAttribute("profile", userService.getProfile(user));
            model.addAttribute("rentals", rentalService.findRentalsByUser(user));
            return "user/profile";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Wachtwoord succesvol gewijzigd"
        );

        return "redirect:/profile";
    }

}
