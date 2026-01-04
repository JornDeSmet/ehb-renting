package com.example.ehbrenting.controller.admin;

import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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


    @GetMapping
    public String listEquipment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<EquipmentDTO> equipmentPage;

        if (keyword != null && !keyword.isBlank()) {
            equipmentPage = equipmentService.search(
                    keyword,
                    PageRequest.of(page, size, Sort.by("name").ascending())
            );
            model.addAttribute("keyword", keyword);
        } else {
            equipmentPage = equipmentService.findAll(
                    PageRequest.of(page, size, Sort.by("name").ascending())
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
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("title", "Materiaal Beheer");

        return "admin/equipment-list";
    }



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


    @PostMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteById(id);
        return "redirect:/admin/equipment?success=deleted";
    }
}
