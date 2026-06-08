package com.biblioteka.config;

import com.biblioteka.domain.user.User;
import com.biblioteka.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed a user if the database table is completely empty
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            // Scramble "admin123" securely using our BCrypt bean!
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");

            userRepository.save(admin);
            System.out.println(
                    "====== SYSTEM: Default admin account created! (Username: admin / Password: admin123) ======");
        }
    }
}