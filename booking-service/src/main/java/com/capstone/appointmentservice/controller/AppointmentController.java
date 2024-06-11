package com.capstone.appointmentservice.controller;

import com.capstone.appointmentservice.DTO.v1.AppointmentDTO;
import com.capstone.appointmentservice.DTO.v1.GetAppointmentDTO;
import com.capstone.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity
                .status(appointmentService.createAppointment(appointmentDTO))
                .build();
    }

    @GetMapping
    public List<GetAppointmentDTO> getFilteredAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate>  dateFilter,
            @RequestParam Optional<String> patientId,
            @RequestParam Optional<String> doctorId
    ) {
        return appointmentService.getFilteredAppointments(dateFilter, patientId, doctorId);
    }

    @GetMapping("/{appointmentId}")
    public GetAppointmentDTO getAppointment(@PathVariable UUID appointmentId) {
        return appointmentService.getAppointment(appointmentId);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<HttpStatus> updateAppointment(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody AppointmentDTO appointmentDTO
    ) {
        return ResponseEntity
                .status(appointmentService.updateAppointment(appointmentId, appointmentDTO))
                .build();
    }

}
