package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CatalogController {

    private final EquipmentService equipmentService;

    public CatalogController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/catalog")
    public String showCatalog(@RequestParam(required = false) String category,
                              @RequestParam(required = false) String search,
                              Model model) {

        List<EquipmentDTO> equipmentList;

        if (search != null && !search.isEmpty()) {
            equipmentList = equipmentService.searchEquipment(search);
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.isEmpty()) {
            equipmentList = equipmentService.getEquipmentByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            equipmentList = equipmentService.getAllEquipment();
        }

        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("categories", equipmentService.getAllCategories());
        model.addAttribute("title", "Catalogus");

        return "catalog/catalog";
    }

    @GetMapping("/equipment/{id}")
    public String equipmentDetails(@PathVariable Long id, Model model) {

        EquipmentDTO equipment = equipmentService.getEquipmentById(id)
                .orElseThrow(() -> new RuntimeException("Materiaal niet gevonden"));

        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(30);

        List<AvailabilityDTO> availabilityList =
                equipmentService.getAvailability(id, from, to);

        boolean isAvailable =
                equipment.isActive() &&
                        equipment.getQuantity() > 0 &&
                        availabilityList.stream().anyMatch(a -> a.getAvailable() > 0);

        model.addAttribute("equipment", equipment);
        model.addAttribute("availabilityList", availabilityList);
        model.addAttribute("isAvailable", isAvailable);

        return "catalog/equipment-details";
    }
}
