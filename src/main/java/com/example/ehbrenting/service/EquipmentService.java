package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    private EquipmentDTO convertToDTO(Equipment equipment) {
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

    private Equipment convertToEntity(EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setId(equipmentDTO.getId());
        equipment.setName(equipmentDTO.getName());
        equipment.setDescription(equipmentDTO.getDescription());
        equipment.setCategory(equipmentDTO.getCategory());
        equipment.setPricePerDay(equipmentDTO.getPricePerDay());
        equipment.setQuantity(equipmentDTO.getQuantity());
        equipment.setImageUrl(equipmentDTO.getImageUrl());
        equipment.setActive(equipmentDTO.isActive());
        return equipment;
    }

    public List<EquipmentDTO> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipmentDTO> getAvailableEquipment() {
        return equipmentRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipmentDTO> getEquipmentByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return getAvailableEquipment();
        }
        return equipmentRepository.findByCategoryAndActiveTrue(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipmentDTO> searchEquipment(String query) {
        return equipmentRepository.findByNameContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EquipmentDTO> getEquipmentById(Long id) {
        return equipmentRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public void saveEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = convertToEntity(equipmentDTO);
        equipmentRepository.save(equipment);
    }

    @Transactional
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return equipmentRepository.findAll().stream()
                .map(Equipment::getCategory)
                .distinct()
                .sorted()
                .toList();
    }
}