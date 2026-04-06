package com.hms.hospitalmanagement.service;

import com.hms.hospitalmanagement.entity.*;
import com.hms.hospitalmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // 🔥 CREATE RECORD
    public MedicalRecord createRecord(Long patientId,
                                      Long doctorId,
                                      Long appointmentId,
                                      MedicalRecord record) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = null;

        if (appointmentId != null) {
            appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
        }

        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setAppointment(appointment);
        record.setVisitDate(LocalDate.now());

        return medicalRecordRepository.save(record);
    }

    // 🔥 GET PATIENT HISTORY
    public List<MedicalRecord> getPatientRecords(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    // 🔥 GET DOCTOR RECORDS
    public List<MedicalRecord> getDoctorRecords(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }
}