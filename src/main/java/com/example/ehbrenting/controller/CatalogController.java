package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.service.EquipmentAvailabilityService;
import com.example.ehbrenting.service.EquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CatalogController {

    private final EquipmentService equipmentService;
    private final EquipmentAvailabilityService availabilityService;

    public CatalogController(
            EquipmentService equipmentService,
            EquipmentAvailabilityService availabilityService
    ) {
        this.equipmentService = equipmentService;
        this.availabilityService = availabilityService;
    }


    @GetMapping("/catalog")
    public String showCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Model model
    ) {
        Page<EquipmentDTO> equipmentPage;

        if (search != null && !search.isBlank()) {
            equipmentPage = equipmentService.searchByName(
                    search,
                    PageRequest.of(page, size)
            );
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.isBlank()) {
            equipmentPage = equipmentService.findActiveByCategory(
                    category,
                    PageRequest.of(page, size)
            );
            model.addAttribute("selectedCategory", category);
        } else {
            equipmentPage = equipmentService.findAllActive(
                    PageRequest.of(page, size)
            );
        }

        int totalPages = equipmentPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        if (totalPages <= 5) {
            startPage = 0;
            endPage = totalPages - 1;
        }

        model.addAttribute("equipmentList", equipmentPage.getContent());
        model.addAttribute("categories", equipmentService.findActiveCategories());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("title", "Catalogus");

        return "catalog/catalog";
    }




    @GetMapping("/equipment/{id}")
    public String equipmentDetails(
            @PathVariable Long id,
            Model model
    ) {
        EquipmentDTO equipment = equipmentService.findByIdOrThrow(id);

        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(30);

        List<AvailabilityDTO> availabilityList =
                availabilityService.calculateAvailability(id, from, to);

        boolean isAvailable =
                equipment.isActive() &&
                        availabilityList.stream().anyMatch(a -> a.getAvailable() > 0);

        model.addAttribute("equipment", equipment);
        model.addAttribute("availabilityList", availabilityList);
        model.addAttribute("isAvailable", isAvailable);

        return "catalog/equipment-details";
    }
}
