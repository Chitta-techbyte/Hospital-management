package com.hms.hospitalmanagement.service;

import com.hms.hospitalmanagement.dto.DashboardDTO;
import com.hms.hospitalmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public DashboardDTO getAdminDashboard() {

        DashboardDTO dto = new DashboardDTO();

        dto.setTotalPatients(patientRepository.count());
        dto.setTotalDoctors(doctorRepository.count());
        dto.setTotalAppointments(appointmentRepository.count());
        dto.setTodayAppointments(
                appointmentRepository.countByDate(LocalDate.now())
        );

        return dto;
    }
}