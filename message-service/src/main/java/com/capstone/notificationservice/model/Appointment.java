package com.capstone.notificationservice.model;

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

    private String doctorId;

    private String patientId;
}
