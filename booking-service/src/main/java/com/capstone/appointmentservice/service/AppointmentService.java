package com.capstone.appointmentservice.service;

import com.capstone.appointmentservice.DTO.v1.AppointmentDTO;
import com.capstone.appointmentservice.DTO.v1.GetAppointmentDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AppointmentService {

    /**
     * Creates a new appointment between the doctor and patient
     *
     * @param appointmentDTO DTO for the appointment object
     * @return HttpStatus code
     */
    HttpStatus createAppointment(AppointmentDTO appointmentDTO);

    /**
     *
     * Returns the upcoming appointments of either the patient
     * or the doctor
     *
     * @param dateFilter date which to filter the results by
     * @param patientId id of the patient with appointments
     * @param doctorId id of the doctor with appointments
     * @return List of appointments
     */
    List<GetAppointmentDTO> getFilteredAppointments(Optional<LocalDate> dateFilter, Optional<String> patientId, Optional<String> doctorId);


    /**
     * Updates an existing appointment
     *
     * @param appointmentId appointment id
     * @param appointmentDTO appointment object with updated values
     * @return HttpStatus code
     */
    HttpStatus updateAppointment(UUID appointmentId, AppointmentDTO appointmentDTO);

    /**
     * Gets an appointment by id
     *
     * @param appointmentId appointment id
     */
    GetAppointmentDTO getAppointment(UUID appointmentId);
}

