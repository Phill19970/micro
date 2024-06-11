package com.capstone.userservice.service.patient;

import com.capstone.userservice.dto.v1.PatientSignUp;
import com.capstone.userservice.exception.ResourceNotFoundException;
import com.capstone.userservice.model.Patient;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    /**
     * Get a specific patient based on the
     * patientId passed
     *
     * @param patientId
     * @return A patient object
     * @throws ResourceNotFoundException when the id cannot be found
     */
    Patient getPatient(String patientId);

    /**
     * Save a patient to the database
     *
     * @param patientSignUp patient DTO
     * @param doctorId id of the doctor
     * @return HttpStatus code
     */
    HttpStatus savePatient(PatientSignUp patientSignUp, Optional<String> doctorId);

    /**
     * Get all patients associate with a doctor
     * @param doctorId id of the doctor
     * @return list of patients
     */
    List<Patient> getAllPatients(String doctorId);
}
