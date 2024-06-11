package com.capstone.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecord {
    private UUID id;
    private LocalDate checkInDate;
    private String notes;
    private String disease;
    private String status;
    private String roomNo;

    private String doctorId;
    private String patientId;
    private UUID appointmentId;

}
