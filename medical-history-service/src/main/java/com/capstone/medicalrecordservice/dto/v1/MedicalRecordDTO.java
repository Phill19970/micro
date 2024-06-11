package com.capstone.medicalrecordservice.dto.v1;

import com.capstone.medicalrecordservice.model.Prescription;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDTO {

    @NotNull(message = "Doctor ID is required")
    private String doctorId;

    @NotNull(message = "Patient ID is required")
    private String patientId;

    @NotNull(message = "Appointment ID is required")
    private UUID appointmentId;

    @NotNull(message = "Check in date is required")
    @PastOrPresent(message = "Check in date cannot be in the future")
    private LocalDate checkInDate;

    private String notes;

    private String disease;

    private String status;

    @NotBlank(message = "Room number is required")
    private String roomNo;

    private List<Prescription> prescriptions;

}
