package com.capstone.appointmentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static com.capstone.appointmentservice.util.UpdateUtil.updateHelper;

@Data
@ToString(exclude = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment must be in the future or present")
    private LocalDate appointmentDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String doctorId;

    private String patientId;



    public void updateObject(Appointment appointment) {
        updateHelper(appointment.getAppointmentDate(), this::setAppointmentDate);
        updateHelper(appointment.getStartTime(), this::setStartTime);
        updateHelper(appointment.getEndTime(), this::setEndTime);
        updateHelper(appointment.getReason(), this::setReason);
    }

}
