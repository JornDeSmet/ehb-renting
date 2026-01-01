package com.example.ehbrenting.controller.admin;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    private final RentalService rentalService;

    public AdminRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @GetMapping
    public String listRentals(Model model) {
        model.addAttribute("rentals", rentalService.findAll());
        model.addAttribute("title", "Reservatiebeheer");
        return "admin/rentals";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        Rental rental = rentalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservatie niet gevonden"));

        model.addAttribute("rental", rental);
        model.addAttribute("title", "Reservatie Bewerken");

        return "admin/edit-rental";
    }


    @PostMapping("/edit/{id}")
    public String updateRental(
            @PathVariable Long id,
            @Valid @ModelAttribute("rental") Rental rental,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", "Reservatie Bewerken");
            return "admin/edit-rental";
        }

        rentalService.updateRental(id, rental);
        return "redirect:/admin/rentals?success=updated";
    }


    @PostMapping("/delete/{id}")
    public String deleteRental(@PathVariable Long id) {
        rentalService.deleteById(id);
        return "redirect:/admin/rentals?success=deleted";
    }
}
