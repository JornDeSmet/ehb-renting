package com.example.ehbrenting.controller.admin;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    private final RentalService rentalService;

    public AdminRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @GetMapping
    public String listRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Rental.RentalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model
    ) {
        Page<Rental> rentalPage = rentalService.searchAndFilter(
                keyword,
                status,
                startDate,
                endDate,
                PageRequest.of(page, size)
        );

        int totalPages = rentalPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        if (totalPages <= 5) {
            startPage = 0;
            endPage = totalPages - 1;
        }

        model.addAttribute("rentals", rentalPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // filters behouden
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        model.addAttribute("statuses", Rental.RentalStatus.values());
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
