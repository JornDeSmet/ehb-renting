package com.example.ehbrenting.config;

import com.example.ehbrenting.model.User;
import com.example.ehbrenting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
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
}
