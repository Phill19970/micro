package com.capstone.userservice.service.doctor;


import com.capstone.userservice.dto.v1.DoctorDTO;
import com.capstone.userservice.dto.v1.DoctorSignUp;
import com.capstone.userservice.exception.ResourceExistsException;
import com.capstone.userservice.exception.ResourceNotFoundException;
import com.capstone.userservice.mapper.DoctorMapper;
import com.capstone.userservice.model.Doctor;
import com.capstone.userservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;




    @Override
    public Doctor getDoctor(String doctorId) {

        //TODO: Add exception handling when id is not found
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + doctorId + " does not exist"));
    }


    @Override
    public Page<DoctorDTO> getAllDoctors(Optional<String> specialization, Optional<String> department, Optional<String> name, Integer size, Integer page) {

        Specification<Doctor> doctorSpecification = Specification
                .where(specialization
                        .map(DoctorSpecification::hasSpecialization)
                        .orElse(null)
                )
                .and(department
                        .map(DoctorSpecification::inDepartment)
                        .orElse(null)
                )
                .and(name
                        .map(DoctorSpecification::hasName)
                        .orElse(null)
                );


        Pageable pageable = PageRequest.of(page, size);

        return doctorRepository.findAll(doctorSpecification, pageable)
                .map(doctorMapper::toDTO);
    }

    @Override
    public HttpStatus saveDoctor(DoctorSignUp doctorSignUp) {
        //Extract the user's JWT token and get their user id
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaim("uid");

        if (doctorRepository.existsById(userId)) {
            log.info("User already exists");
            throw new ResourceExistsException("User already exists");
        }

        Doctor doctor = doctorMapper.toEntity(doctorSignUp);
        doctor.setId(userId);

        doctorRepository.save(doctor);


        return HttpStatus.CREATED;
    }
}
