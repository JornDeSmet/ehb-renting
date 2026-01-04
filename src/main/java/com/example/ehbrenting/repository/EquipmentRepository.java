package com.example.ehbrenting.repository;

import com.example.ehbrenting.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {


    @Query("select distinct e.category from Equipment e where e.active = true")
    List<String> findDistinctActiveCategories();

    Page<Equipment> findAllByOrderByNameAsc(Pageable pageable);

    Page<Equipment> findByNameContainingIgnoreCaseOrderByNameAsc(
            String name,
            Pageable pageable
    );
    Page<Equipment> findByActiveTrue(Pageable pageable);

    Page<Equipment> findByCategoryAndActiveTrue(String category, Pageable pageable);

    Page<Equipment> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
}
