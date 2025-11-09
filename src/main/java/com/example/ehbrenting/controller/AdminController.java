package com.example.ehbrenting.controller;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RentalService rentalService;

    public AdminController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/rentals")
    public String manageRentals(Model model) {
        List<Rental> rentals = rentalService.findAll();
        model.addAttribute("rentals", rentals);
        return "admin/rentals";
    }

    @PostMapping("/rentals/delete/{id}")
    public String deleteRental(@PathVariable Long id) {
        rentalService.deleteById(id);
        return "redirect:/admin/rentals";
    }

    @GetMapping("/rentals/edit/{id}")
    public String editRentalForm(@PathVariable Long id, Model model) {
        rentalService.findById(id).ifPresent(rental -> model.addAttribute("rental", rental));
        return "admin/edit-rental";
    }

    @PostMapping("/rentals/edit/{id}")
    public String updateRental(@PathVariable Long id, @ModelAttribute Rental rental) {
        rentalService.updateRental(id, rental);
        return "redirect:/admin/rentals";
    }
}
