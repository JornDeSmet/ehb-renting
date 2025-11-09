package com.example.ehbrenting.service;

import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> findRentalsByUser(User user) {
        return rentalRepository.findByUser(user);
    }
}
