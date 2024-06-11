package com.capstone.medicalrecordservice.service;

import com.capstone.medicalrecordservice.client.AppointmentClient;
import com.capstone.medicalrecordservice.client.UserClient;
import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.exception.MedicalRecordExists;
import com.capstone.medicalrecordservice.exception.ResourceNotFoundException;
import com.capstone.medicalrecordservice.mapper.MedicalRecordMapper;
import com.capstone.medicalrecordservice.mapper.MedicalRecordMapperImpl;
import com.capstone.medicalrecordservice.model.*;
import com.capstone.medicalrecordservice.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    MedicalRecordService medicalRecordService;

    @Mock
    MedicalRecordRepository medicalRepository;
    @Mock
    UserClient userClient;
    @Mock
    AppointmentClient appointmentClient;

    MedicalRecordMapper medicalMapper = new MedicalRecordMapperImpl();

    MedicalRecord testMedicalRecord;
    MedicalRecordDTO testMedicalDTO;
    Appointment testAppointment;


    @BeforeEach
    void setUp() {
        medicalRecordService = new MedicalRecordServiceImpl(medicalRepository, medicalMapper, appointmentClient, userClient);

        testMedicalRecord = MedicalRecord.builder()
                .id(UUID.randomUUID())
                .build();

        testAppointment = Appointment.builder()
                .id(UUID.randomUUID())
                .build();

        testMedicalDTO = MedicalRecordDTO.builder()
                .appointmentId(UUID.randomUUID())
                .prescriptions(List.of(
                        Prescription.builder()
                                .medication("Medication")
                                .dosage(1)
                                .total(BigDecimal.valueOf(123L))
                                .startDate(LocalDate.now())
                                .endDate(LocalDate.now())
                                .build()
                ))
                .patientId("1L")
                .doctorId("1L")
                .build();

    }

    @Test
    void getMedicalRecords() {

        given(medicalRepository.findAllByPatientId(anyString()))
                .willReturn(Arrays.asList(testMedicalRecord));

        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecords("1L");

        then(medicalRepository).should(times(1)).findAllByPatientId("1L");
        assertThat(medicalRecords).isEqualTo(List.of(testMedicalRecord));
    }


    @Test
    void createMedicalRecord() {
        given(medicalRepository.existsByAppointmentId(any(UUID.class)))
                .willReturn(false);

        given(medicalRepository.save(any(MedicalRecord.class)))
                .willReturn(testMedicalRecord);

        given(appointmentClient.getAppointment(any(UUID.class)))
                .willReturn(testAppointment);

        given(userClient.getDoctor(anyString()))
                .willReturn(new Doctor());

        given(userClient.getPatient(anyString()))
                .willReturn(new Patient());

        HttpStatus status = medicalRecordService.createMedicalRecord("1L", testMedicalDTO);

        then(medicalRepository).should(times(1)).save(any(MedicalRecord.class));
        assertThat(status).isEqualTo(HttpStatus.CREATED);
    }



    @Test
    public void testCreateMedicalRecordThrowsMedicalRecordExistsException() {
        // Arrange
        Long nonExistentAppointmentId = 100L;
        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setAppointmentId(UUID.randomUUID());

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAppointmentId(UUID.randomUUID());

        given(medicalRepository.existsByAppointmentId(any(UUID.class)))
                .willReturn(true);

        // Act & Assert
        assertThrows(MedicalRecordExists.class, () -> {
            medicalRecordService.createMedicalRecord("1L", medicalRecordDTO);
        });
    }

    @Test
    void getMedicalRecordForAppointmentId() {

        given(medicalRepository.findByAppointmentId(any(UUID.class)))
                .willReturn(Optional.of(testMedicalRecord));

        MedicalRecord recordsForAppointment = medicalRecordService.getMedicalRecordsForAppointment(UUID.randomUUID());


        then(medicalRepository).should(times(1)).findByAppointmentId(any(UUID.class));
        assertThat(recordsForAppointment).isEqualTo(testMedicalRecord);

    }

    @Test
    void getMedicalRecordForAppointmentId_ThrowsNotFoundException() {

        given(medicalRepository.findByAppointmentId(any(UUID.class)))
                .willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> medicalRecordService.getMedicalRecordsForAppointment(UUID.randomUUID()));

    }
}