package com.example.ehbrenting.repository;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("""
        SELECT r FROM Rental r
        WHERE r.equipment.id = :equipmentId
          AND r.status = 'CONFIRMED'
          AND r.startDate <= :endDate
          AND r.endDate >= :startDate
    """)
    List<Rental> findRentalsForEquipmentInDateRange(
            @Param("equipmentId") Long equipmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<Rental> findByUserAndStatus(User user, Rental.RentalStatus status);
    List<Rental> findByUser(User user);


    @Query("""
        SELECT r FROM Rental r
        WHERE
            (:keyword IS NULL OR
             LOWER(r.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(r.equipment.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:status IS NULL OR r.status = :status)
        AND (:startDate IS NULL OR r.startDate >= :startDate)
        AND (:endDate IS NULL OR r.endDate <= :endDate)
        ORDER BY r.startDate DESC
    """)
    Page<Rental> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("status") Rental.RentalStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
