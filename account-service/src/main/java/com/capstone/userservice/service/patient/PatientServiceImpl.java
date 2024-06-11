package com.capstone.userservice.service.patient;


import com.capstone.userservice.dto.v1.PatientSignUp;
import com.capstone.userservice.exception.ResourceExistsException;
import com.capstone.userservice.exception.ResourceNotFoundException;
import com.capstone.userservice.mapper.PatientMapper;
import com.capstone.userservice.model.Doctor;
import com.capstone.userservice.model.Patient;
import com.capstone.userservice.repository.DoctorRepository;
import com.capstone.userservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientMapper patientMapper;



    @Override
    public Patient getPatient(String patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + patientId + " does not exist"));
    }

    @Override
    public HttpStatus savePatient(PatientSignUp patientSignUp, Optional<String> doctorId) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaim("uid");

        if (patientRepository.existsById(userId)) {
            log.info("Patient already exists");
            throw new ResourceExistsException("Email already exists");
        }

        //Mapping the DTO to the entity and setting the doctor
        //to the entity
        Patient patient = patientMapper.toEntity(patientSignUp);
        patient.setId(userId);


        //Check if doctor exists
        if (doctorId.isPresent()) {

            if (!doctorRepository.existsById(doctorId.get())) {
                throw new ResourceNotFoundException("Doctor with id " + doctorId.get() + " does not exists");
            }
            else {
                patient.setDoctor(
                        Doctor.builder()
                                .id(doctorId.get())
                                .build()
                );
            }

        }

        patientRepository.save(patient);

        return HttpStatus.CREATED;
    }

    @Override
    public List<Patient> getAllPatients(String doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor with id " + doctorId + " does not exist");
        }


        return patientRepository
                .findAllByDoctorId(doctorId);
    }
}
