package com.example.ehbrenting.repository;

import com.example.ehbrenting.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByActiveTrue();

    List<Equipment> findByCategoryAndActiveTrue(String category);

    List<Equipment> findByNameContainingIgnoreCase(String name);

    @Query("select distinct e.category from Equipment e where e.active = true")
    List<String> findDistinctActiveCategories();
}
