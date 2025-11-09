package com.example.ehbrenting.repository;

import com.example.ehbrenting.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByActiveTrue();

    List<Equipment> findByCategoryAndActiveTrue(String category);

    List<Equipment> findByNameContainingIgnoreCase(String name);
}
