package com.hms.hospitalmanagement.service;

import com.hms.hospitalmanagement.entity.User;
import com.hms.hospitalmanagement.exception.DuplicateEmailException;
import com.hms.hospitalmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {

        try {
            System.out.println("Incoming user: " + user.getEmail());

            if (user.getEmail() == null || user.getPassword() == null) {
                throw new RuntimeException("Email or password missing");
            }

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new RuntimeException("Email already registered");
            }

            if (passwordEncoder == null) {
                throw new RuntimeException("PasswordEncoder NOT initialized");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getRole() == null || user.getRole().isEmpty()) {
                throw new RuntimeException("Role is required");
            }

            String role = user.getRole().toUpperCase();

            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }

            if (!role.equals("ROLE_ADMIN") &&
                    !role.equals("ROLE_DOCTOR") &&
                    !role.equals("ROLE_OPERATOR")) {
                throw new RuntimeException("Invalid role: " + role);
            }

            user.setRole(role);

            User savedUser = userRepository.save(user);

            System.out.println("User saved successfully");

            return savedUser;

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 THIS WILL SHOW REAL ERROR IN LOGS
            throw new RuntimeException("Register failed: " + e.getMessage());
        }
    }

    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}