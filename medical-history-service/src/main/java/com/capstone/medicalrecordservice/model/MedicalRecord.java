package com.capstone.medicalrecordservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Check in date is required")
    @PastOrPresent(message = "Check in date cannot be in the future")
    private LocalDate checkInDate;
    private String notes;
    private String disease;
    private String status;

    @NotBlank(message = "Room number is required")
    private String roomNo;

    private String doctorId;

    private String patientId;

    private UUID appointmentId;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;
}
