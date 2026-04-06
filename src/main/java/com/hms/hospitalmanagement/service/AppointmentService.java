package com.hms.hospitalmanagement.service;

import com.hms.hospitalmanagement.dto.AppointmentDTO;
import com.hms.hospitalmanagement.entity.*;
import com.hms.hospitalmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                                       Long userId,
                                       Appointment appointment) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (appointmentRepository.findByDoctorIdAndDateAndTimeSlot(
                doctorId,
                appointment.getDate(),
                appointment.getTimeSlot()
        ).isPresent()) {
            throw new RuntimeException("Time slot already booked");
        }

        if (appointment.getTimeSlot().getMinute() % 15 != 0) {
            throw new RuntimeException("Invalid time slot (use 15 min interval)");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setCreatedBy(user);
        appointment.setStatus("SCHEDULED");

        return appointmentRepository.save(appointment);
    }

    // 🔥 UPDATE STATUS
    public Appointment updateStatus(Long appointmentId, String status) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(status.toUpperCase());

        return appointmentRepository.save(appointment);
    }

    // 🔥 RESCHEDULE
    public Appointment reschedule(Long appointmentId, LocalDate newDate, LocalTime newTime) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 🚨 Check slot availability
        if (appointmentRepository.findByDoctorIdAndDateAndTimeSlot(
                appointment.getDoctor().getId(),
                newDate,
                newTime
        ).isPresent()) {
            throw new RuntimeException("Time slot already booked");
        }

        if (newTime.getMinute() % 15 != 0) {
            throw new RuntimeException("Invalid time slot");
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