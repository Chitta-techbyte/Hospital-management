package com.hms.hospitalmanagement.config;

import com.hms.hospitalmanagement.entity.User;
import com.hms.hospitalmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByEmail("chittaraja16@gmail.com").isEmpty()) {

                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("chittaraja16@gmail.com");
                admin.setPassword(passwordEncoder.encode("Chitta@1615"));
                admin.setRole("ROLE_ADMIN");

                userRepository.save(admin);

                System.out.println("✅ Admin created!");
            }
        };
    }
}