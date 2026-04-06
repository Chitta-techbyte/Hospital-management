package com.hms.hospitalmanagement.service;

import com.hms.hospitalmanagement.entity.Doctor;
import com.hms.hospitalmanagement.entity.User;
import com.hms.hospitalmanagement.repository.DoctorRepository;
import com.hms.hospitalmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    // 🔥 UPDATED METHOD
    public Doctor addDoctor(Long userId, Doctor doctor) {

        // 1. Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Validate role
        if (!user.getRole().equals("ROLE_DOCTOR")) {
            throw new RuntimeException("User is not assigned DOCTOR role");
        }

        // 3. Link doctor with user
        doctor.setUser(user);

        // 4. Save doctor
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}