package com.capstone.userservice.controller;


import com.capstone.userservice.dto.v1.PatientSignUp;
import com.capstone.userservice.model.Patient;
import com.capstone.userservice.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DOCTOR')")
    public List<Patient> getAllPatients(@RequestParam(required = true) String doctorId) {
        return patientService.getAllPatients(doctorId);
    }


    @GetMapping("/{patientId}")
    public Patient getPatient(@PathVariable String patientId) {
        return patientService.getPatient(patientId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<HttpStatus> savePatient(@RequestParam(required = false) Optional<String> doctorId,
                                                  @Valid @RequestBody PatientSignUp patientSignUp
                                                  ) {

        return ResponseEntity
                .status(patientService.savePatient(patientSignUp, doctorId))
                .build();
    }



}
