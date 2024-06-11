package com.capstone.medicalrecordservice.controller;

import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.model.MedicalRecord;
import com.capstone.medicalrecordservice.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalService;

    public MedicalRecordController(MedicalRecordService medicalService) {
        this.medicalService = medicalService;
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords(@RequestParam String patientId) {
        return medicalService.getMedicalRecords(patientId);
    }

    @GetMapping("/{appointmentId}")
    public MedicalRecord getMedicalRecords(@PathVariable UUID appointmentId) {
        return medicalService.getMedicalRecordsForAppointment(appointmentId);
    }

    @PostMapping("/{patientId}")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<HttpStatus> createMedicalRecord(
            @PathVariable String patientId,
            @Valid @RequestBody MedicalRecordDTO medicalRecordDTO
    ) {

        return ResponseEntity
                .status(medicalService.createMedicalRecord(patientId, medicalRecordDTO))
                .build();
    }
}
