package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.service.EquipmentAvailabilityService;
import com.example.ehbrenting.service.EquipmentService;
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

    /* ===================== CATALOG ===================== */

    @GetMapping("/catalog")
    public String showCatalog(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Model model
    ) {
        List<EquipmentDTO> equipmentList;

        if (search != null && !search.isBlank()) {
            equipmentList = equipmentService.searchByName(search);
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.isBlank()) {
            equipmentList = equipmentService.findActiveByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            equipmentList = equipmentService.findAllActive();
        }

        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("categories", equipmentService.findActiveCategories());
        model.addAttribute("title", "Catalogus");

        return "catalog/catalog";
    }


    /* ===================== DETAILS ===================== */

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
