package com.capstone.medicalrecordservice.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@ToString(exclude = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {
    private UUID id;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private Doctor doctor;
    private Patient patient;
    private MedicalRecord medicalRecord;
}
