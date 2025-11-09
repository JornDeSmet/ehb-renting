package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CatalogController {

    private final EquipmentService equipmentService;

    @Autowired
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
        model.addAttribute("equipment", equipment);
        return "catalog/equipment-details";
    }

    @GetMapping("/admin/equipment")
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("title", "Materiaal Beheer");
        return "admin/equipment-list";
    }

    @GetMapping("/admin/equipment/add")
    public String showAddForm(Model model) {
        model.addAttribute("equipmentDto", new EquipmentDTO());
        model.addAttribute("title", "Materiaal Toevoegen");
        return "admin/equipment-form";
    }

    @PostMapping("/admin/equipment/add")
    public String addEquipment(@Valid @ModelAttribute("equipmentDto") EquipmentDTO equipmentDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Materiaal Toevoegen");
            return "admin/equipment-form";
        }

        equipmentService.saveEquipment(equipmentDto);
        return "redirect:/admin/equipment?success=added";
    }

    @GetMapping("/admin/equipment/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EquipmentDTO equipmentDto = equipmentService.getEquipmentById(id)
                .orElseThrow(() -> new RuntimeException("Materiaal niet gevonden"));

        model.addAttribute("equipmentDto", equipmentDto);
        model.addAttribute("title", "Materiaal Bewerken");
        return "admin/equipment-form";
    }

    @PostMapping("/admin/equipment/edit/{id}")
    public String updateEquipment(@PathVariable Long id,
                                  @Valid @ModelAttribute("equipmentDto") EquipmentDTO equipmentDto,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Materiaal Bewerken");
            return "admin/equipment-form";
        }

        equipmentDto.setId(id);
        equipmentService.saveEquipment(equipmentDto);
        return "redirect:/admin/equipment?success=updated";
    }

    @PostMapping("/admin/equipment/delete/{id}")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return "redirect:/admin/equipment?success=deleted";
    }
}