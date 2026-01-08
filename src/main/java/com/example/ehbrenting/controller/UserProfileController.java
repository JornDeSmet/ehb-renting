package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.user.ChangePasswordDTO;
import com.example.ehbrenting.dto.user.UserProfileDTO;
import com.example.ehbrenting.exceptions.InvalidPasswordException;
import com.example.ehbrenting.exceptions.PasswordMismatchException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService userService;
    private final RentalService rentalService;

    public UserProfileController(UserService userService,
                                 RentalService rentalService) {
        this.userService = userService;
        this.rentalService = rentalService;
    }


    @GetMapping
    public String profile(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("profile", userService.getProfile(user));
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        return "user/profile";
    }


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
            return "user/profile";
        }

        userService.updateProfile(user, profileDTO);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Gegevens succesvol aangepast"
        );

        return "redirect:/profile";
    }


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
            return "user/profile";
        }

        try {
            userService.changePassword(user, dto);
        } catch (InvalidPasswordException e) {
            result.rejectValue(
                    "currentPassword",
                    null,
                    e.getMessage()
            );
        } catch (PasswordMismatchException e) {
            result.rejectValue(
                    "confirmNewPassword",
                    null,
                    e.getMessage()
            );
        }

        if (result.hasErrors()) {
            model.addAttribute("profile", userService.getProfile(user));
            return "user/profile";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Wachtwoord succesvol gewijzigd"
        );

        return "redirect:/profile";
    }




    @GetMapping("/rentals")
    public String myRentals(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("rentals", rentalService.findRentalsByUser(user));
        model.addAttribute("title", "Mijn Reserveringen");
        return "user/rentals";
    }
}
