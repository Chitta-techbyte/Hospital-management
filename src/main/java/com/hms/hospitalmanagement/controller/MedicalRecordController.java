package com.hms.hospitalmanagement.controller;

import com.hms.hospitalmanagement.entity.MedicalRecord;
import com.hms.hospitalmanagement.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    // 🔥 DOCTOR ADDS RECORD
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/create")
    public MedicalRecord createRecord(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long appointmentId,
            @RequestBody MedicalRecord record) {

        return medicalRecordService.createRecord(
                patientId, doctorId, appointmentId, record);
    }

    // 🔥 PATIENT HISTORY
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getPatientRecords(@PathVariable Long patientId) {
        return medicalRecordService.getPatientRecords(patientId);
    }

    // 🔥 DOCTOR RECORDS
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public List<MedicalRecord> getDoctorRecords(@PathVariable Long doctorId) {
        return medicalRecordService.getDoctorRecords(doctorId);
    }
}