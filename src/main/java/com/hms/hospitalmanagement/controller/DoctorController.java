package com.hms.hospitalmanagement.controller;

import com.hms.hospitalmanagement.entity.Doctor;
import com.hms.hospitalmanagement.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ✅ FIXED METHOD
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Doctor addDoctor(@RequestParam Long userId,
                            @RequestBody Doctor doctor) {
        System.out.println("USER ID RECEIVED: " + userId);
        return doctorService.addDoctor(userId, doctor);
    }

    @GetMapping
    public List<Doctor> getDoctors() {
        return doctorService.getAllDoctors();
    }
}