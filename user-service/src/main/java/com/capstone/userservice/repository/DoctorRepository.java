package com.capstone.userservice.repository;

import com.capstone.userservice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String>, JpaSpecificationExecutor<Doctor> {
    Optional<Doctor> findByEmail(String email);
    boolean existsByEmail(String email);
}
