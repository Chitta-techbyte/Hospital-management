package com.hms.hospitalmanagement.service;

import com.hms.hospitalmanagement.dto.AppointmentDTO;
import com.hms.hospitalmanagement.entity.*;
import com.hms.hospitalmanagement.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    // 🔥 BOOK APPOINTMENT
    public Appointment bookAppointment(Long patientId,
                                       Long doctorId,
                                       LocalDate date,
                                       LocalTime timeSlot) {

        // 🔍 Fetch patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 🔍 Fetch doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 🔐 Get logged-in user safely
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🚫 Prevent double booking
        if (appointmentRepository.existsByDoctorIdAndDateAndTimeSlot(
                doctorId, date, timeSlot
        )) {
            throw new RuntimeException("Time slot already booked");
        }

        // 🚫 Validate time slot (15 min interval)
        if (timeSlot.getMinute() % 15 != 0) {
            throw new RuntimeException("Invalid time slot (15 min interval)");
        }

        // 🚫 Prevent past booking
        if (date.isBefore(LocalDate.now())) {
            throw new RuntimeException("Invalid date");
        }

        // 🧱 Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setCreatedBy(user);
        appointment.setStatus("SCHEDULED");

        return appointmentRepository.save(appointment);
    }

    // 🔥 UPDATE STATUS
    public Appointment updateStatus(Long appointmentId, String status) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // ✅ Validate allowed status
        List<String> allowed = List.of("SCHEDULED", "COMPLETED", "CANCELLED");

        if (!allowed.contains(status.toUpperCase())) {
            throw new RuntimeException("Invalid status");
        }

        appointment.setStatus(status.toUpperCase());

        return appointmentRepository.save(appointment);
    }

    // 🔥 RESCHEDULE
    public Appointment reschedule(Long appointmentId, LocalDate newDate, LocalTime newTime) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 🚫 Prevent past date
        if (newDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Invalid date");
        }

        // 🚫 Validate time slot
        if (newTime.getMinute() % 15 != 0) {
            throw new RuntimeException("Invalid time slot");
        }

        // 🚫 Avoid duplicate check for same slot
        if (!appointment.getDate().equals(newDate) ||
                !appointment.getTimeSlot().equals(newTime)) {

            if (appointmentRepository.existsByDoctorIdAndDateAndTimeSlot(
                    appointment.getDoctor().getId(),
                    newDate,
                    newTime
            )) {
                throw new RuntimeException("Time slot already booked");
            }
        }

        appointment.setDate(newDate);
        appointment.setTimeSlot(newTime);

        return appointmentRepository.save(appointment);
    }

    // 🔥 CANCEL
    public Appointment cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus("CANCELLED");

        return appointmentRepository.save(appointment);
    }

    // 🔥 DTO CONVERTER
    private AppointmentDTO convertToDTO(Appointment appointment) {

        AppointmentDTO dto = new AppointmentDTO();

        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());
        dto.setTimeSlot(appointment.getTimeSlot());
        dto.setStatus(appointment.getStatus());

        dto.setPatientName(appointment.getPatient().getName());
        dto.setDoctorName(appointment.getDoctor().getUser().getName());

        return dto;
    }

    // 🔥 GET BY DATE
    public List<AppointmentDTO> getDoctorAppointmentsByDate(Long doctorId, LocalDate date) {

        return appointmentRepository.findByDoctorIdAndDate(doctorId, date)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // 🔥 GET ALL
    public List<AppointmentDTO> getAllDoctorAppointments(Long doctorId) {

        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
}