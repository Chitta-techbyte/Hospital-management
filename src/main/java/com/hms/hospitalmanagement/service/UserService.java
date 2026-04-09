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

        // ✅ Check duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already registered");
        }

        // ✅ Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 🚨 IMPORTANT: Validate role
        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new RuntimeException("Role is required");
        }

        // Optional: enforce allowed roles
        String role = user.getRole().toUpperCase();

// 🔥 Convert to Spring format
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

// ✅ Validate role
        if (!role.equals("ROLE_ADMIN") &&
                !role.equals("ROLE_DOCTOR") &&
                !role.equals("ROLE_OPERATOR")) {
            throw new RuntimeException("Invalid role");
        }

        user.setRole(role);


        return userRepository.save(user);
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