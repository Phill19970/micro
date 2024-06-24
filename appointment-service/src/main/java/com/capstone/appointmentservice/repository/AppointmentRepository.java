package com.capstone.appointmentservice.repository;

import com.capstone.appointmentservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findAllByPatientId(String patientId);
    List<Appointment> findAllByDoctorId(String doctorId);

}
