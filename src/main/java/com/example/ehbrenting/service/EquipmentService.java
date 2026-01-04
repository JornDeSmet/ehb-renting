package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.mapper.EquipmentMapper;
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

    private final EquipmentRepository repository;
    private final EquipmentMapper mapper;

    public EquipmentService(EquipmentRepository repository,
                            EquipmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<EquipmentDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<EquipmentDTO> findAllActive() {
        return repository.findByActiveTrue().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<EquipmentDTO> findActiveByCategory(String category) {
        if (category == null || category.isBlank()) {
            return findAllActive();
        }
        return repository.findByCategoryAndActiveTrue(category).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<EquipmentDTO> searchByName(String query) {
        return repository.findByNameContainingIgnoreCase(query).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public EquipmentDTO findByIdOrThrow(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
    }

    @Transactional
    public void createOrUpdate(EquipmentDTO dto) {
        repository.save(mapper.toEntity(dto));
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<String> findActiveCategories() {
        return repository.findDistinctActiveCategories();
    }

}
