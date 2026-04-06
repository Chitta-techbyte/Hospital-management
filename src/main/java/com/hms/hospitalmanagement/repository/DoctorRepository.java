package com.hms.hospitalmanagement.repository;

import com.hms.hospitalmanagement.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}