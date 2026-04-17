package com.hms.hospitalmanagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDTO {

    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDate date;
    private LocalTime timeSlot;
    private String status;

    // ✅ Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTimeSlot() { return timeSlot; }
    public void setTimeSlot(LocalTime timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}