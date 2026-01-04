package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.dto.PageRangeDTO;
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
        Page<EquipmentDTO> equipmentPage =
                equipmentService.getCatalogPage(page, size, category, search);

        PageRangeDTO pageRange =
                equipmentService.calculatePageRange(page, equipmentPage.getTotalPages());

        model.addAttribute("equipmentList", equipmentPage.getContent());
        model.addAttribute("categories", equipmentService.findActiveCategories());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipmentPage.getTotalPages());
        model.addAttribute("startPage", pageRange.startPage());
        model.addAttribute("endPage", pageRange.endPage());

        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchQuery", search);
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
