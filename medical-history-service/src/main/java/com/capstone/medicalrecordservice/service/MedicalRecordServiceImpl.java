package com.capstone.medicalrecordservice.service;

import com.capstone.medicalrecordservice.client.AppointmentClient;
import com.capstone.medicalrecordservice.client.UserClient;
import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.exception.MedicalRecordExists;
import com.capstone.medicalrecordservice.exception.ResourceNotFoundException;
import com.capstone.medicalrecordservice.mapper.MedicalRecordMapper;
import com.capstone.medicalrecordservice.model.Appointment;
import com.capstone.medicalrecordservice.model.Doctor;
import com.capstone.medicalrecordservice.model.MedicalRecord;
import com.capstone.medicalrecordservice.model.Patient;
import com.capstone.medicalrecordservice.repository.MedicalRecordRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRepository;
    private final MedicalRecordMapper medicalMapper;
    private final AppointmentClient appointmentClient;
    private final UserClient userClient;


    @Override
    public List<MedicalRecord> getMedicalRecords(String patientId) {
        return medicalRepository.findAllByPatientId(
                patientId
        );
    }

    @Override
    @Transactional
    public HttpStatus createMedicalRecord(String patientId, MedicalRecordDTO medicalRecordDTO) {

        if (Boolean.TRUE.equals(medicalRepository.existsByAppointmentId(medicalRecordDTO.getAppointmentId()))) {
            log.error("Medical record for appointment {} already exists" , medicalRecordDTO.getAppointmentId());
            throw new MedicalRecordExists("Medical record for appointment id "+ medicalRecordDTO.getAppointmentId() + " already exists");
        }


        //Change the DTO to the entity
        MedicalRecord medicalRecord = medicalMapper.toEntity(medicalRecordDTO);

        //Check if the id's exists
        try {
            Appointment appointment = appointmentClient.getAppointment(medicalRecordDTO.getAppointmentId());
            Patient patient = userClient.getPatient(medicalRecordDTO.getPatientId());
            Doctor doctor =  userClient.getDoctor(medicalRecordDTO.getDoctorId());

        } catch (FeignException.NotFound e) {
            String message = "";
            if (e.getMessage().contains("AppointmentClient")) {
                message = "Could not find appointment " + medicalRecordDTO.getAppointmentId();
                log.error("Could not find appointment {}", medicalRecordDTO.getAppointmentId());
            } else if (e.getMessage().contains("UserClient")) {
                message = "Could not find patient " + medicalRecordDTO.getPatientId() + " or doctor " + medicalRecordDTO.getDoctorId();
                log.error("Could not find patient {}", medicalRecordDTO.getPatientId());
            }

            throw new ResourceNotFoundException(message);
        }



        //Update the relationship of the prescriptions
        medicalRecord.getPrescriptions()
                .forEach(prescription -> prescription.setMedicalRecord(medicalRecord));

        MedicalRecord savedMedicalRecord = medicalRepository.save(medicalRecord);
        log.info("Created medical record {}", savedMedicalRecord.getId());

        return HttpStatus.CREATED;
    }

    @Override
    public MedicalRecord getMedicalRecordsForAppointment(UUID appointmentId) {
        return medicalRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> {
                    log.error("Could not find medical record for appointment {}", appointmentId);
                     return new ResourceNotFoundException("Could not find medical record for appointment " + appointmentId);
                });
    }
}
