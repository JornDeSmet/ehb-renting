package com.example.ehbrenting.controller.admin;

import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/equipment")
public class AdminEquipmentController {

    private final EquipmentService equipmentService;

    public AdminEquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /* ===================== LIST ===================== */

    @GetMapping
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.findAll());
        model.addAttribute("title", "Materiaal Beheer");
        return "admin/equipment-list";
    }

    /* ===================== ADD ===================== */

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("equipmentDto", new EquipmentDTO());
        model.addAttribute("title", "Materiaal Toevoegen");
        return "admin/equipment-form";
    }

    @PostMapping("/add")
    public String addEquipment(
            @Valid @ModelAttribute("equipmentDto") EquipmentDTO equipmentDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Materiaal Toevoegen");
            return "admin/equipment-form";
        }

        equipmentService.createOrUpdate(equipmentDto);
        return "redirect:/admin/equipment?success=added";
    }

    /* ===================== EDIT ===================== */

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        EquipmentDTO equipmentDto = equipmentService.findByIdOrThrow(id);

        model.addAttribute("equipmentDto", equipmentDto);
        model.addAttribute("title", "Materiaal Bewerken");
        return "admin/equipment-form";
    }

    @PostMapping("/edit/{id}")
    public String updateEquipment(
            @PathVariable Long id,
            @Valid @ModelAttribute("equipmentDto") EquipmentDTO equipmentDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Materiaal Bewerken");
            return "admin/equipment-form";
        }

        equipmentDto.setId(id);
        equipmentService.createOrUpdate(equipmentDto);
        return "redirect:/admin/equipment?success=updated";
    }

    /* ===================== DELETE ===================== */

    @PostMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteById(id);
        return "redirect:/admin/equipment?success=deleted";
    }
}
