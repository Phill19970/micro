package com.capstone.userservice.controller;


import com.capstone.userservice.dto.v1.DoctorDTO;
import com.capstone.userservice.dto.v1.DoctorSignUp;
import com.capstone.userservice.model.Doctor;
import com.capstone.userservice.service.doctor.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Validated
@RestController
@Slf4j
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    Page<DoctorDTO> getAllDoctors(
            @RequestParam Optional<String> specialization,
            @RequestParam Optional<String> department,
            @RequestParam Optional<String> name,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page
            ) {
        return doctorService.getAllDoctors(
                specialization, department, name, size, page
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus saveDoctor(@RequestBody DoctorSignUp doctorSignUp) {

        return doctorService.saveDoctor(doctorSignUp);

    }

    @GetMapping("/{doctorId}")
    public Doctor getDoctor(@PathVariable String doctorId) {
        return doctorService.getDoctor(doctorId);
    }


}
