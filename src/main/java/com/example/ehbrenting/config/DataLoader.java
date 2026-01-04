package com.example.ehbrenting.config;

import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.EquipmentRepository;
import com.example.ehbrenting.repository.RentalRepository;
import com.example.ehbrenting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final RentalRepository rentalRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    public DataLoader(
            UserRepository userRepository,
            EquipmentRepository equipmentRepository,
            RentalRepository rentalRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.rentalRepository = rentalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        loadUsers();
        loadEquipment();
        loadRentals();
    }


    private void loadUsers() {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@ehbschool.be");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Marie");
        admin.setLastName("Admin");
        admin.setRole(User.Role.ADMIN);
        admin.setEnabled(true);
        userRepository.save(admin);

        for (int i = 1; i <= 99; i++) {
            User user = new User();
            user.setUsername("student" + i);
            user.setEmail("student" + i + "@ehbschool.be");
            user.setPassword(passwordEncoder.encode("password"));
            user.setFirstName("Student");
            user.setLastName("Nr" + i);
            user.setRole(User.Role.STUDENT);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }


    private void loadEquipment() {
        if (equipmentRepository.count() > 0) return;

        List<String> categories = List.of(
                "Audio", "Verlichting", "Video", "Podium", "Kabels", "Effecten"
        );

        for (int i = 1; i <= 100; i++) {
            Equipment equipment = new Equipment();
            equipment.setName("Equipment " + i);
            equipment.setDescription("Demo equipment item nummer " + i);
            equipment.setCategory(categories.get(random.nextInt(categories.size())));
            equipment.setPricePerDay(BigDecimal.valueOf(5 + random.nextInt(50)));
            equipment.setQuantity(1 + random.nextInt(20));
            equipment.setActive(random.nextBoolean());
            equipment.setImageUrl("https://via.placeholder.com/300");

            equipmentRepository.save(equipment);
        }
    }


    private void loadRentals() {
        if (rentalRepository.count() > 0) return;

        List<User> users = userRepository.findAll();
        List<Equipment> equipmentList = equipmentRepository.findAll();

        for (int i = 1; i <= 100; i++) {
            User user = users.get(random.nextInt(users.size()));
            Equipment equipment = equipmentList.get(random.nextInt(equipmentList.size()));

            LocalDate startDate = LocalDate.now().minusDays(random.nextInt(30));
            LocalDate endDate = startDate.plusDays(1 + random.nextInt(7));

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setEquipment(equipment);
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);
            rental.setQuantity(1 + random.nextInt(
                    Math.max(1, equipment.getQuantity())
            ));
            rental.setStatus(randomStatus());

            rentalRepository.save(rental);
        }
    }

    private Rental.RentalStatus randomStatus() {
        Rental.RentalStatus[] statuses = Rental.RentalStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
}
