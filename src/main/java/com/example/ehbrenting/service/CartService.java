package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.CartItemDto;
import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.EquipmentRepository;
import com.example.ehbrenting.repository.RentalRepository;
import com.example.ehbrenting.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public CartService(RentalRepository rentalRepository, EquipmentRepository equipmentRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addToCart(String username, CartItemDto cartItemDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Equipment equipment = equipmentRepository.findById(cartItemDto.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setEquipment(equipment);
        rental.setQuantity(cartItemDto.getQuantity());
        rental.setStartDate(cartItemDto.getStartDate());
        rental.setEndDate(cartItemDto.getEndDate());
        rental.setStatus(Rental.RentalStatus.IN_CART);

        rentalRepository.save(rental);
    }

    public List<Rental> getCartItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return rentalRepository.findByUserAndStatus(user, Rental.RentalStatus.IN_CART);
    }

    @Transactional
    public void confirmOrder(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        List<Rental> cartItems = rentalRepository.findByUserAndStatus(user, Rental.RentalStatus.IN_CART);

        for (Rental item : cartItems) {
            item.setStatus(Rental.RentalStatus.CONFIRMED);
            rentalRepository.save(item);
        }
    }
}
