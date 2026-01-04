package com.example.ehbrenting.controller.admin;

import com.example.ehbrenting.dto.user.AdminCreateUserDTO;
import com.example.ehbrenting.exceptions.EmailAlreadyExistsException;
import com.example.ehbrenting.exceptions.UsernameAlreadyExistsException;
import com.example.ehbrenting.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // ================= LIST =================
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", adminUserService.findAll());
        model.addAttribute("title", "Gebruikersbeheer");
        return "admin/user-list";
    }

    // ================= CREATE =================
    @GetMapping("/add")
    public String showCreateForm(Model model) {
        model.addAttribute("userDto", new AdminCreateUserDTO());
        model.addAttribute("showPassword", true);
        model.addAttribute("action", "/admin/users/add");
        model.addAttribute("title", "Gebruiker toevoegen");
        return "admin/user-form";
    }

    @PostMapping("/add")
    public String createUser(
            @Valid @ModelAttribute("userDto") AdminCreateUserDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return returnCreateForm(model);
        }

        try {
            adminUserService.createUser(dto);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", null, e.getMessage());
        } catch (EmailAlreadyExistsException e) {
            result.rejectValue("email", null, e.getMessage());
        }

        if (result.hasErrors()) {
            return returnCreateForm(model);
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Gebruiker succesvol aangemaakt"
        );
        return "redirect:/admin/users";
    }

    // ================= EDIT =================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("userDto", adminUserService.getUserForEdit(id));
        model.addAttribute("showPassword", false);
        model.addAttribute("action", "/admin/users/edit/" + id);
        model.addAttribute("title", "Gebruiker bewerken");
        return "admin/user-form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("userDto") AdminCreateUserDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return returnEditForm(model, id);
        }

        try {
            adminUserService.updateUser(id, dto);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", null, e.getMessage());
        } catch (EmailAlreadyExistsException e) {
            result.rejectValue("email", null, e.getMessage());
        }

        if (result.hasErrors()) {
            return returnEditForm(model, id);
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Gebruiker succesvol aangepast"
        );
        return "redirect:/admin/users";
    }

    // ================= ENABLE / DISABLE =================
    @PostMapping("/{id}/toggle")
    public String toggleEnabled(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        adminUserService.toggleEnabled(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Gebruikersstatus aangepast"
        );
        return "redirect:/admin/users";
    }

    // ================= HELPERS =================
    private String returnCreateForm(Model model) {
        model.addAttribute("showPassword", true);
        model.addAttribute("action", "/admin/users/add");
        model.addAttribute("title", "Gebruiker toevoegen");
        return "admin/user-form";
    }

    private String returnEditForm(Model model, Long id) {
        model.addAttribute("showPassword", false);
        model.addAttribute("action", "/admin/users/edit/" + id);
        model.addAttribute("title", "Gebruiker bewerken");
        return "admin/user-form";
    }
}
