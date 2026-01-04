package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.CartItemDTO;
import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> findRentalsByUser(User user) {
        return rentalRepository.findByUser(user);
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public void deleteById(Long id) {
        rentalRepository.deleteById(id);
    }

    public Optional<Rental> findById(Long id) {
        return rentalRepository.findById(id);
    }


    public void updateRental(Long id, Rental rental) {
        rentalRepository.findById(id).ifPresent(existingRental -> {
            existingRental.setQuantity(rental.getQuantity());
            existingRental.setStatus(rental.getStatus());
            rentalRepository.save(existingRental);
        });
    }

    public Page<Rental> searchAndFilter(
            String keyword,
            Rental.RentalStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        return rentalRepository.searchAndFilter(
                (keyword == null || keyword.isBlank()) ? null : keyword,
                status,
                startDate,
                endDate,
                pageable
        );
    }
}
