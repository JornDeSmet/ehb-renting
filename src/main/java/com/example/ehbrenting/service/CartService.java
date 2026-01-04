package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.CartItemDTO;
import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.EquipmentRepository;
import com.example.ehbrenting.repository.RentalRepository;
import com.example.ehbrenting.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentAvailabilityService availabilityService;

    public CartService(
            RentalRepository rentalRepository,
            EquipmentRepository equipmentRepository,
            EquipmentAvailabilityService availabilityService
    ) {
        this.rentalRepository = rentalRepository;
        this.equipmentRepository = equipmentRepository;
        this.availabilityService = availabilityService;
    }


    @Transactional
    public void addToCart(User user, CartItemDTO dto) {

        availabilityService.validateRentalRequest(
                dto.getEquipmentId(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getQuantity()
        );

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setEquipment(equipment);
        rental.setQuantity(dto.getQuantity());
        rental.setStartDate(dto.getStartDate());
        rental.setEndDate(dto.getEndDate());
        rental.setStatus(Rental.RentalStatus.IN_CART);

        rentalRepository.save(rental);
    }


    public List<Rental> getCartItems(User user) {
        return rentalRepository.findByUserAndStatus(
                user,
                Rental.RentalStatus.IN_CART
        );
    }


    @Transactional
    public void confirmOrder(User user) {

        List<Rental> cartItems =
                rentalRepository.findByUserAndStatus(
                        user,
                        Rental.RentalStatus.IN_CART
                );

        for (Rental item : cartItems) {
            item.setStatus(Rental.RentalStatus.CONFIRMED);
        }
    }
}
