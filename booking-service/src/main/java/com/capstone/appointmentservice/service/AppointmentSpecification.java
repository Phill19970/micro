package com.capstone.appointmentservice.service;

import com.capstone.appointmentservice.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class AppointmentSpecification {

    /**
     * Method create a criteria which checks for the specific
     * date the client is looking for.
     *
     * @param dateFilter date by which the wants to see their appointments
     * @return
     */
    public static Specification<Appointment> hasDate(LocalDate dateFilter) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("appointmentDate"),
                        dateFilter
                );
    }

    /**
     * Method create a criteria which checks for the specific
     * doctor the client is looking for.
     *
     * @param doctorId id by which the wants to see their appointments
     * @return
     */
    public static Specification<Appointment> hasDoctor(String doctorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("doctorId"),
                        doctorId
                );
    }

    /**
     * Method create a criteria which checks for the specific
     * patient the client is looking for.
     *
     * @param patientId id by which the wants to see their appointments
     * @return
     */
    public static Specification<Appointment> hasPatient(String patientId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("patientId"),
                        patientId
                );
    }

}
