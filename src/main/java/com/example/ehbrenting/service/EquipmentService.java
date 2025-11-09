package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.repository.EquipmentRepository;
import com.example.ehbrenting.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository, RentalRepository rentalRepository) {
        this.equipmentRepository = equipmentRepository;
        this.rentalRepository = rentalRepository;
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

    public List<AvailabilityDTO> getAvailability(Long equipmentId, LocalDate from, LocalDate to) {
        List<AvailabilityDTO> availabilityList = new ArrayList<>();
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isEmpty()) {
            return availabilityList;
        }
        Equipment equipment = equipmentOpt.get();
        int totalQuantity = equipment.getQuantity();

        List<Rental> rentals = rentalRepository.findRentalsForEquipmentInDateRange(equipmentId, from, to);

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            int rentedQuantity = 0;
            for (Rental rental : rentals) {
                if (!date.isBefore(rental.getStartDate()) && !date.isAfter(rental.getEndDate())) {
                    rentedQuantity += rental.getQuantity();
                }
            }
            availabilityList.add(new AvailabilityDTO(date, totalQuantity - rentedQuantity));
        }

        return availabilityList;
    }
}
