package com.capstone.appointmentservice.service;

import com.capstone.appointmentservice.DTO.v1.AppointmentDTO;
import com.capstone.appointmentservice.DTO.v1.GetAppointmentDTO;
import com.capstone.appointmentservice.client.MedicalRecordClient;
import com.capstone.appointmentservice.client.UserClient;
import com.capstone.appointmentservice.config.MqConfig;
import com.capstone.appointmentservice.exception.AppointmentConflictException;
import com.capstone.appointmentservice.exception.ResourceNotFoundException;
import com.capstone.appointmentservice.mapper.AppointmentMapper;
import com.capstone.appointmentservice.model.Appointment;
import com.capstone.appointmentservice.model.Doctor;
import com.capstone.appointmentservice.model.MedicalRecord;
import com.capstone.appointmentservice.model.Patient;
import com.capstone.appointmentservice.repository.AppointmentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final UserClient userClient;
    private final MedicalRecordClient medicalRecordClient;
    private final AppointmentRepository appointmentRepository;
    private final RabbitTemplate rabbitTemplate;


    @Override
    public GetAppointmentDTO getAppointment(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map((appointment) -> {
                    GetAppointmentDTO returnDTO = appointmentMapper.toReturnDTO(appointment);

                    try {
                        returnDTO.setMedicalRecord(
                                medicalRecordClient.getMedicalRecords(appointment.getId())
                        );
                    }
                    catch (FeignException.NotFound e) {
                        log.info("Medical Record not found with appointment id {}", appointmentId);
                        returnDTO.setMedicalRecord(null);
                    }

                    return returnDTO;
                } )
                .orElseThrow(() -> {
                    log.info("Appointment id = {} does not exists", appointmentId);
                    return new ResourceNotFoundException("Appointment id = " + appointmentId + " does not exists");
                });
    }

    @Override
    public HttpStatus createAppointment(AppointmentDTO appointmentDTO) {

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);

        /*
            Check if the doctor and patient exists
         */
        try {
            Patient patient = userClient.getPatient(appointmentDTO.getPatientId());

            Doctor doctor = userClient.getDoctor(appointmentDTO.getDoctorId());

        } catch (FeignException.NotFound e) {
            log.info("Doctor id = {} or patient id = {} does not exists", appointmentDTO.getDoctorId(), appointmentDTO.getPatientId());
            throw new ResourceNotFoundException("Doctor id = " + appointmentDTO.getDoctorId() + " or patient id = " + appointmentDTO.getPatientId() + " does not exists");
        }

        /*
        If any appointment has a conflict then return conflict
         */
        if (Boolean.TRUE.equals(hasAppointmentConflict(appointment))){
            log.info("Appointment has conflict = {}", appointment);
            throw new AppointmentConflictException("Appointment has conflict");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment saved = {}", savedAppointment);

        rabbitTemplate.convertAndSend(MqConfig.EXCHANGE, MqConfig.ROUTING_KEY, savedAppointment);
        log.info("Message sent successfully to RabbitMq");

        return HttpStatus.CREATED;
    }


    @Override
    public List<GetAppointmentDTO> getFilteredAppointments(Optional<LocalDate> dateFilter, Optional<String> patientId, Optional<String> doctorId) {
        if (patientId.isEmpty() && doctorId.isEmpty()) {
            log.info("No patient or doctor id specified");
            throw new ResourceNotFoundException("No patient or doctor id specified");
        }

        //Add the date filter if client added in req param
        Specification<Appointment> appointmentSpecification =
                Specification
                        .where(dateFilter
                                .map(AppointmentSpecification::hasDate)
                                .orElse(null)
                        )
                        .and(doctorId
                                .map(AppointmentSpecification::hasDoctor)
                                .orElse(null)
                        )
                        .and(patientId
                                .map(AppointmentSpecification::hasPatient)
                                .orElse(null)
                        );

        return appointmentRepository.findAll(appointmentSpecification)
                .stream()
                .map((appointment) -> {
                    GetAppointmentDTO returnDTO = appointmentMapper.toReturnDTO(appointment);

                    try {
                        MedicalRecord medicalRecord = medicalRecordClient.getMedicalRecords(appointment.getId());
                        returnDTO.setMedicalRecord(medicalRecord);
                    } catch (Exception e) {
                        returnDTO.setMedicalRecord(null);
                        log.info("Appointment id = {} does not exists", appointment.getId());
                    }


                    return returnDTO;
                })
                .toList();
    }


    @Override
    public HttpStatus updateAppointment(UUID appointmentId, AppointmentDTO appointmentDTO) {

        Appointment updatedAppointment = appointmentMapper.toEntity(appointmentDTO);


        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    log.info("Appointment with id {} does not exist", appointmentId);
                    return new ResourceNotFoundException("Appointment with id " + appointmentId + " does not exist");
                });


        //Update Values in the object if not null
        appointment.updateObject(updatedAppointment);

        if (Boolean.TRUE.equals(hasAppointmentConflict(appointment))){
            log.info("Appointment has conflict = {}", appointment);
            throw new AppointmentConflictException("Appointment has conflict");
        }


        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment updated = {}", savedAppointment);

        rabbitTemplate.convertAndSend(MqConfig.EXCHANGE, MqConfig.ROUTING_KEY, savedAppointment);
        log.info("Message sent successfully to RabbitMq");

        return HttpStatus.OK;
    }


    private Boolean hasAppointmentConflict(Appointment appointment) {

        List<Appointment> patientAppointments = appointmentRepository.findAllByPatientId(appointment.getPatientId());
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorId(appointment.getDoctorId());


        //Check if any existing appointments is clashing with the new one
        boolean patientMatch = patientAppointments.stream()
                .filter(app -> !Objects.equals(app.getId(), appointment.getId()))
                .filter(app -> app.getAppointmentDate().equals(appointment.getAppointmentDate()))
                .anyMatch(app -> isAppointmentClashing(app, appointment));

        boolean doctorMatch = doctorAppointments.stream()
                .filter(app -> !Objects.equals(app.getId(), appointment.getId()))
                .filter(app -> app.getAppointmentDate().equals(appointment.getAppointmentDate()))
                .anyMatch(app -> isAppointmentClashing(app, appointment));

        /*
        If any is true, there's a conflict in the appointment
        Only return false when both are false.
         */
        return patientMatch || doctorMatch;
    }

    //Checks to see if any conflict between two given appointments
    private Boolean isAppointmentClashing(Appointment appointment1, Appointment appointment2) {
        return appointment1.getStartTime().isBefore(appointment2.getEndTime())
                && appointment2.getStartTime().isBefore(appointment1.getEndTime());
    }

}
