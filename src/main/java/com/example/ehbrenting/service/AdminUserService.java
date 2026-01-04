package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.user.AdminCreateUserDTO;
import com.example.ehbrenting.exceptions.EmailAlreadyExistsException;
import com.example.ehbrenting.exceptions.UsernameAlreadyExistsException;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllByOrderByUsernameAsc(pageable);
    }

    public Page<User> search(String keyword, Pageable pageable) {
        return userRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        keyword, keyword, keyword, keyword, pageable
                );
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void createUser(AdminCreateUserDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameAlreadyExistsException("Gebruikersnaam bestaat al");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email bestaat al");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setEnabled(dto.isEnabled());

        userRepository.save(user);
    }

    public AdminCreateUserDTO getUserForEdit(Long id) {
        User user = findByIdOrThrow(id);

        AdminCreateUserDTO dto = new AdminCreateUserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());

        return dto;
    }

    public void updateUser(Long id, AdminCreateUserDTO dto) {
        User user = findByIdOrThrow(id);

        if (!user.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameAlreadyExistsException("Gebruikersnaam bestaat al");
        }

        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email bestaat al");
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setEnabled(dto.isEnabled());

        userRepository.save(user);
    }

    public void toggleEnabled(Long id) {
        User user = findByIdOrThrow(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }
}
