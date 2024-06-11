package com.capstone.userservice.repository;

import com.capstone.userservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    List<Patient> findAllByDoctorId(String doctorId);
    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);
}
