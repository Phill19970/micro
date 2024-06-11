package com.capstone.userservice.service.patient;

import com.capstone.userservice.dto.v1.PatientSignUp;
import com.capstone.userservice.exception.ResourceNotFoundException;
import com.capstone.userservice.mapper.PatientMapper;
import com.capstone.userservice.mapper.PatientMapperImpl;
import com.capstone.userservice.model.Doctor;
import com.capstone.userservice.model.Patient;
import com.capstone.userservice.repository.DoctorRepository;
import com.capstone.userservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    Jwt jwt;

    PatientService patientService;

    @Mock
    PatientRepository patientRepository;
    @Mock
    DoctorRepository doctorRepository;

    PatientMapper patientMapper = new PatientMapperImpl();

    Patient testPatient;
    PatientSignUp testPatientDTO;
    Doctor testDoctor;

    @BeforeEach
    void setUp() {
        patientService = new PatientServiceImpl(patientRepository, doctorRepository, patientMapper);

        testPatient = Patient.builder()
                .name("name")
                .age(1)
                .address("address")
                .phoneNumber("1234567890")
                .bloodGroup("A")
                .religion("Religion")
                .occupation("Occupation")
                .gender('M')
                .maritalStatus("Single")
                .description("Description")
                .doctor(Doctor.builder().build())
                .email("test@gmail.com")
                .build();

        testPatientDTO = PatientSignUp.builder()
                .email("test@gmail.com")
                .name("name")
                .address("address")
                .phoneNumber("1234567890")
                .age(1)
                .bloodGroup("A")
                .religion("religion")
                .occupation("Work")
                .gender('M')
                .maritalStatus("Single")
                .description("description")
                .build();

        testDoctor = Doctor.builder().build();
    }

    @Test
    void getPatient() {
        given(patientRepository.findById(anyString()))
                .willReturn(Optional.of(testPatient));

        Patient patient = patientService.getPatient("1L");

        assertThat(patient).isEqualTo(testPatient);
    }

    @Test
    public void testGetPatientWhenPatientDoesNotExist() {
        String patientId = "2L";

        // Mock the patientRepository behavior to return an empty Optional (patient not found)
        given(patientRepository.findById(patientId)).willReturn(Optional.empty());

        // Call the method you want to test and expect an exception
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatient(patientId));
        // Add more assertions or verifications as per your test case requirements
    }


    @Test
    void getAllPatients() {
        given(doctorRepository.existsById(anyString()))
                .willReturn(true);

        given(patientRepository.findAllByDoctorId(anyString()))
                .willReturn(List.of(testPatient));


        List<Patient> allPatients = patientService.getAllPatients("1L");


        assertThat(allPatients).isEqualTo(List.of(testPatient));
    }

    @Test
    public void testGetAllPatients_DoctorIdDoesNotExist() {
        String doctorId = "1L";
        given(doctorRepository.existsById(doctorId)).willReturn(false);


        assertThrows(ResourceNotFoundException.class, () -> patientService.getAllPatients(doctorId));
    }
}