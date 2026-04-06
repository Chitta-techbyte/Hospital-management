package com.hms.hospitalmanagement.repository;

import com.hms.hospitalmanagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 🔥 Prevent double booking
    Optional<Appointment> findByDoctorIdAndDateAndTimeSlot(
            Long doctorId,
            LocalDate date,
            LocalTime timeSlot
    );

    // 🔥 Get all appointments of a doctor for a specific date
    List<Appointment> findByDoctorIdAndDate(
            Long doctorId,
            LocalDate date
    );

    // 🔥 Get all appointments of a doctor
    List<Appointment> findByDoctorId(Long doctorId);
    long countByDate(LocalDate date);
}