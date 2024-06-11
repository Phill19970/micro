package com.capstone.medicalrecordservice.client;

import com.capstone.medicalrecordservice.model.Appointment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * @author Javaughn Stephenson
 * @since 02/08/2023
 */

@FeignClient(name = "appointment-service")
public interface AppointmentClient {

    @GetMapping("/api/v1/appointments/{appointmentId}")
    public Appointment getAppointment(@PathVariable UUID appointmentId);

}
