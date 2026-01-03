package com.example.ehbrenting.mapper;

import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.model.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    public EquipmentDTO toDTO(Equipment equipment) {
        return new EquipmentDTO(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getCategory(),
                equipment.getPricePerDay(),
                equipment.getQuantity(),
                equipment.getImageUrl(),
                equipment.isActive()
        );
    }

    public Equipment toEntity(EquipmentDTO dto) {
        Equipment equipment = new Equipment();
        equipment.setId(dto.getId());
        equipment.setName(dto.getName());
        equipment.setDescription(dto.getDescription());
        equipment.setCategory(dto.getCategory());
        equipment.setPricePerDay(dto.getPricePerDay());
        equipment.setQuantity(dto.getQuantity());
        equipment.setImageUrl(dto.getImageUrl());
        equipment.setActive(dto.isActive());
        return equipment;
    }
}
