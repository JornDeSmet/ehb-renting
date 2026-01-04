package com.example.ehbrenting.service;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.mapper.EquipmentMapper;
import com.example.ehbrenting.repository.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

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

    public Page<EquipmentDTO> findAll(Pageable pageable) {
        return repository.findAllByOrderByNameAsc(pageable)
                .map(mapper::toDTO);
    }

    public Page<EquipmentDTO> search(String keyword, Pageable pageable) {
        return repository
                .findByNameContainingIgnoreCaseOrderByNameAsc(keyword, pageable)
                .map(mapper::toDTO);
    }

    public Page<EquipmentDTO> findAllActive(Pageable pageable) {
        return repository.findByActiveTrue(pageable)
                .map(mapper::toDTO);
    }

    public Page<EquipmentDTO> findActiveByCategory(String category, Pageable pageable) {
        return repository.findByCategoryAndActiveTrue(category, pageable)
                .map(mapper::toDTO);
    }

    public Page<EquipmentDTO> searchByName(String query, Pageable pageable) {
        return repository.findByNameContainingIgnoreCaseAndActiveTrue(query, pageable)
                .map(mapper::toDTO);
    }



}
