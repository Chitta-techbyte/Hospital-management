package com.hms.hospitalmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialization;

    private String availabilityStart; // "09:00"
    private String availabilityEnd;   // "17:00"

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(String availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public String getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(String availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}