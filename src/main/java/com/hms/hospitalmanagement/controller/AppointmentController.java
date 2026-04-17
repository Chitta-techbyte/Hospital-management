package com.hms.hospitalmanagement.controller;

import com.hms.hospitalmanagement.dto.AppointmentDTO;
import com.hms.hospitalmanagement.dto.AppointmentRequest;
import com.hms.hospitalmanagement.entity.Appointment;
import com.hms.hospitalmanagement.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // 🔥 BOOK
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @PostMapping("/book")
    public Appointment bookAppointment(@RequestBody AppointmentRequest request) {

        return appointmentService.bookAppointment(
                request.getPatientId(),
                request.getDoctorId(),
                request.getDate(),
                request.getTimeSlot()
        );
    }

    // 🔥 UPDATE STATUS
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/status/{id}")
    public Appointment updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return appointmentService.updateStatus(id, status);
    }

    // 🔥 RESCHEDULE
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @PutMapping("/reschedule/{id}")
    public Appointment reschedule(
            @PathVariable Long id,
            @RequestParam String date,
            @RequestParam String time) {

        return appointmentService.reschedule(
                id,
                LocalDate.parse(date),
                LocalTime.parse(time)
        );
    }

    // 🔥 CANCEL
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @PutMapping("/cancel/{id}")
    public Appointment cancel(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    // 🔥 DOCTOR DASHBOARD
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentDTO> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String date) {

        if (date != null && !date.isEmpty()) {
            return appointmentService.getDoctorAppointmentsByDate(
                    doctorId,
                    LocalDate.parse(date)
            );
        }

        return appointmentService.getAllDoctorAppointments(doctorId);
    }
}