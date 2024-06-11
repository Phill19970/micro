package com.capstone.medicalrecordservice.repository;

import com.capstone.medicalrecordservice.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    List<MedicalRecord> findAllByPatientId(String patientId);

    Optional<MedicalRecord> findByAppointmentId(UUID appointmentId);

    Boolean existsByAppointmentId(UUID appointmentId);
}
