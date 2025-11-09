package com.example.ehbrenting.config;

import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.EquipmentRepository;
import com.example.ehbrenting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EquipmentRepository equipmentRepository;

    public DataLoader(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EquipmentRepository equipmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
        loadEquipmentData();
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {
            User student = new User();
            student.setUsername("student");
            student.setEmail("student@ehbschool.be");
            student.setPassword(passwordEncoder.encode("password"));
            student.setFirstName("Jan");
            student.setLastName("Janssens");
            student.setRole(User.Role.STUDENT);
            student.setEnabled(true);
            userRepository.save(student);

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@ehbschool.be");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Marie");
            admin.setLastName("Admin");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
        }
    }

    private void loadEquipmentData() {
        if (equipmentRepository.count() == 0) {
            List<Equipment> equipmentList = List.of(
                    new Equipment(null, "LED Par 64", "Krachtige en veelzijdige LED spot voor podiumverlichting.", "Verlichting", new BigDecimal("15.00"), 20, "https://media.sweetwater.com/api/i/q-82__ha-dd82b1a0a55b0a3c__hmac-467d1a36791814674068a13d95e982343f110927/images/items/750/LEDPar64-large.jpg", true),
                    new Equipment(null, "Shure SM58 Microfoon", "Industriestandaard dynamische microfoon, perfect voor zang en spraak.", "Audio", new BigDecimal("10.00"), 15, "https://media.sweetwater.com/api/i/q-82__ha-29a5629355a1a403__hmac-84c1b36b33a4863e61238344238a395b161cb29d/images/items/750/SM58-large.jpg", true),
                    new Equipment(null, "Behringer X32 Mixer", "Digitale 32-kanaals mixer voor live en studio gebruik.", "Audio", new BigDecimal("50.00"), 3, "https://media.sweetwater.com/api/i/q-82__ha-5cf5637424d8135e__hmac-85f70272a883842b544363358a852e1f40e83c40/images/items/750/X32-large.jpg", true),
                    new Equipment(null, "XLR Kabel 10m", "Standaard 10 meter XLR-kabel voor microfoons en andere audio-apparatuur.", "Kabels", new BigDecimal("2.50"), 50, "https://media.sweetwater.com/api/i/q-82__ha-2d5a52875149f023__hmac-185a317c48e8f81944a1923b1b125d233d455943/images/items/750/XLR10-large.jpg", true),
                    new Equipment(null, "Podiumdeel 2x1m", "Stabiel en robuust podiumdeel van 2x1 meter.", "Podium", new BigDecimal("25.00"), 12, "https://www.stagedrop.nl/wp-content/uploads/2020/12/Prolyte-StageDex-Basic-Line-2x1.jpg", true),
                    new Equipment(null, "ETC Source Four Profielspot", "Professionele profielspot voor haarscherpe projecties.", "Verlichting", new BigDecimal("20.00"), 10, "https://www.fullcompass.com/common/products/original/120605.jpg", true),
                    new Equipment(null, "QSC K12.2 Actieve Speaker", "Krachtige 2000W actieve luidspreker voor FOH of monitor.", "Audio", new BigDecimal("40.00"), 8, "https://media.sweetwater.com/api/i/q-82__ha-362207a87ab4294a__hmac-ef717332b193336394194e138a2d3663734b1049/images/items/750/K12.2-large.jpg", true),
                    new Equipment(null, "HDMI Kabel 15m", "Hoge-kwaliteit 15 meter HDMI-kabel voor video-overdracht.", "Kabels", new BigDecimal("5.00"), 20, "https://media.sweetwater.com/api/i/q-82__ha-3b5c3d3100a10f54__hmac-15f9a74d8d32b1a11c11ddd34a6e122401217c4a/images/items/750/HDMIB15-large.jpg", false),
                    new Equipment(null, "Rookmachine Antari Z-1500", "Professionele rookmachine met hoge output voor sfeereffecten.", "Effecten", new BigDecimal("35.00"), 2, "https://www.highlite.com/media/catalog/product/cache/a558e847a7c8ddb75ed481f26723d843/6/0/60593_2.jpg", true)
            );
            equipmentRepository.saveAll(equipmentList);
        }
    }
}
