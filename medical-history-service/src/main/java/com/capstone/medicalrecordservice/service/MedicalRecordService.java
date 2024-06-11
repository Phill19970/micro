package com.capstone.medicalrecordservice.service;

import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.model.MedicalRecord;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;


public interface MedicalRecordService {

    /**
     * Get all medical records associated to a patient
     *
     * @param patientId patient id
     * @return list of medical records
     */
    List<MedicalRecord> getMedicalRecords(String patientId);

    /**
     * Creates a new medical record
     *
     * @param patientId patient id
     * @param medicalRecordDTO medical record DTO
     * @return HttpStatus
     */
    HttpStatus createMedicalRecord(String patientId, MedicalRecordDTO medicalRecordDTO);

    /**
     * Get a medical record for a specific appointment id
     *
     * @param appointmentId appointment id
     */
    MedicalRecord getMedicalRecordsForAppointment(UUID appointmentId);
}
