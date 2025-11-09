package com.example.ehbrenting.repository;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.equipment.id = :equipmentId AND r.status = 'CONFIRMED' AND r.startDate <= :endDate AND r.endDate >= :startDate")
    List<Rental> findRentalsForEquipmentInDateRange(@Param("equipmentId") Long equipmentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Rental> findByUserAndStatus(User user, Rental.RentalStatus status);

    List<Rental> findByUser(User user);
}
