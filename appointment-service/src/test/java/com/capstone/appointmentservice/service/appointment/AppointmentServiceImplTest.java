package com.capstone.appointmentservice.service.appointment;

import com.capstone.appointmentservice.DTO.v1.AppointmentDTO;
import com.capstone.appointmentservice.DTO.v1.GetAppointmentDTO;
import com.capstone.appointmentservice.client.MedicalRecordClient;
import com.capstone.appointmentservice.client.UserClient;
import com.capstone.appointmentservice.exception.AppointmentConflictException;
import com.capstone.appointmentservice.exception.ResourceNotFoundException;
import com.capstone.appointmentservice.mapper.AppointmentMapper;
import com.capstone.appointmentservice.model.Appointment;
import com.capstone.appointmentservice.model.Doctor;
import com.capstone.appointmentservice.model.Patient;
import com.capstone.appointmentservice.repository.AppointmentRepository;
import com.capstone.appointmentservice.service.AppointmentService;
import com.capstone.appointmentservice.service.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    AppointmentService appointmentService;

    @Mock
    AppointmentMapper appointmentMapper;

    @Mock
    UserClient userClient;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    MedicalRecordClient medicalRecordClient;

    @Mock
    AppointmentRepository appointmentRepository;

    Appointment appointment;

    AppointmentDTO appointmentDTO;

    Patient patient;

    Doctor doctor;
    GetAppointmentDTO getAppointmentDTO;


    @BeforeEach
    void setUp() {

        appointmentService = new AppointmentServiceImpl(appointmentMapper, userClient, medicalRecordClient, appointmentRepository, rabbitTemplate);


        appointmentDTO = AppointmentDTO.builder()
                .doctorId("1L")
                .patientId("1L")
                .build();

        patient = Patient.builder()
                .id("1L")
                .build();

        doctor = Doctor.builder()
                .id("1L")
                .build();

        appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .patientId("1L")
                .doctorId("1L")
                .build();

        getAppointmentDTO = GetAppointmentDTO.builder()
                .build();

    }

    @Test
    void createAppointment() {

        // Given

        // Stubbing the AppointmentMapper's toEntity method to return an Appointment instance
        given(appointmentMapper.toEntity(any(AppointmentDTO.class))).willReturn(appointment);

        // Stubbing the UserClient to return a patient when findById is called
        given(userClient.getPatient("1L")).willReturn(patient);

        // Stubbing the UserClient to return a doctor given() findById is called
        given(userClient.getDoctor("1L")).willReturn(doctor);

        // Stubbing the AppointmentRepository to return the saved appointment given() save is called
        given(appointmentRepository.save(appointment)).willReturn(appointment);

        given(appointmentRepository.findAllByDoctorId(anyString()))
                .willReturn(List.of());


        given(appointmentRepository.findAllByPatientId(anyString()))
                .willReturn(List.of());


        // When
        HttpStatus result = appointmentService.createAppointment(appointmentDTO);

        // Then
        then(userClient).should(times(1)).getDoctor("1L");

        then(userClient).should(times(1)).getPatient("1L");

        then(appointmentRepository).should().save(appointment);

        assertThat(result).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void getFilteredAppointments() {

        // Given
        Optional<LocalDate> dateFilter = Optional.of(LocalDate.of(2023, 7, 20));
        Optional<String> patientId = Optional.of("1L");
        Optional<String> doctorId = Optional.of("1L");
        List<Appointment> expectedAppointments = Collections.singletonList(appointment);

        given(appointmentMapper.toReturnDTO(any(Appointment.class)))
                .willReturn(getAppointmentDTO);
        // Stubbing the AppointmentRepository to return expectedAppointments when findAll is called
        given(appointmentRepository.findAll(any(Specification.class)))
                .willReturn(expectedAppointments);

        given(medicalRecordClient.getMedicalRecords(any(UUID.class)))
                .willReturn(null);

        // When
        List<GetAppointmentDTO> result = appointmentService.getFilteredAppointments(dateFilter, patientId, doctorId);

        // Then
        // Verify that findAll is called on the appointmentRepository with the correct Specification
        then(appointmentRepository).should().findAll(any(Specification.class));
        // Verify that the result matches the expectedAppointments
        assertThat(result).isEqualTo(List.of(getAppointmentDTO));

    }

    @Test
    public void testGetFilteredAppointmentsWithEmptyPatientAndDoctorIds() {
        // Given
        Optional<LocalDate> dateFilter = Optional.of(LocalDate.of(2023, 7, 20));
        Optional<String> patientId = Optional.empty();
        Optional<String> doctorId = Optional.empty();

        // When and Then
        assertThrows(ResourceNotFoundException.class, () -> appointmentService.getFilteredAppointments(dateFilter, patientId, doctorId));
        // Verify that findAll is not called on the appointmentRepository since no patient or doctor id is specified
        then(appointmentRepository).should(never()).findAll(any(Specification.class));
    }

    @Test
    void updateAppointment() {

        // Given
        UUID appointmentId = UUID.randomUUID();

        // Stubbing the AppointmentMapper's toEntity method to return an updated appointment
        Appointment updatedAppointment = Appointment.builder()
                .appointmentDate(LocalDate.now().plusDays(5))
                .build();


        given(appointmentMapper.toEntity(appointmentDTO))
                .willReturn(updatedAppointment);

        // Stubbing the AppointmentRepository to return an existing appointment when findById is called
        given(appointmentRepository.findById(appointmentId))
                .willReturn(Optional.of(appointment));

        // Stubbing the appointmentRepository save method to return the saved appointment
        given(appointmentRepository.save(any(Appointment.class)))
                .willReturn(appointment);

        given(appointmentRepository.findAllByDoctorId(anyString()))
                .willReturn(List.of());


        given(appointmentRepository.findAllByPatientId(anyString()))
                .willReturn(List.of());

        // When
        HttpStatus result = appointmentService.updateAppointment(appointmentId, appointmentDTO);

        // Then
        // Verify that findById is called with the correct appointmentId
        then(appointmentRepository).should().findById(appointmentId);

        // Verify that the appointmentMapper's toEntity method is called with the correct appointmentDTO
        then(appointmentMapper).should().toEntity(appointmentDTO);

        // Verify that the appointment is saved in the repository
        then(appointmentRepository).should().save(any(Appointment.class));

        then(rabbitTemplate).should(times(1)).convertAndSend(anyString(), anyString(), any(Appointment.class));

        // Verify that HttpStatus.OK is returned
        assertThat(result).isEqualTo(HttpStatus.OK);
    }



    @Test
    void testCreateAppointment_AppointmentClashes_ThrowsAppointmentConflictException() {
        //Arrange
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId("1L");
        appointmentDTO.setDoctorId("2L");
        appointmentDTO.setAppointmentDate(LocalDate.of(2023, 7, 25));
        appointmentDTO.setStartTime(LocalTime.of(10, 0));
        appointmentDTO.setEndTime(LocalTime.of(11, 0));


        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setPatientId("1L");
        appointment.setDoctorId("2L");
        appointment.setAppointmentDate(LocalDate.of(2023, 7, 25));
        appointment.setStartTime(LocalTime.of(9, 0));
        appointment.setEndTime(LocalTime.of(10, 30));

        Appointment appointment1 = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setPatientId("1L");
        appointment.setDoctorId("2L");
        appointment1.setAppointmentDate(LocalDate.of(2023, 7, 25));
        appointment1.setStartTime(LocalTime.of(9, 0));
        appointment1.setEndTime(LocalTime.of(10, 30));

        List<Appointment> patientAppointments = new ArrayList<>();
        patientAppointments.add(appointment1);

        List<Appointment> doctorAppointments = new ArrayList<>();
        doctorAppointments.add(appointment1);

        given(appointmentMapper.toEntity(appointmentDTO)).willReturn(appointment);
        given(userClient.getPatient("1L")).willReturn(patient);
        given(userClient.getDoctor("2L")).willReturn(doctor);
        given(appointmentRepository.findAllByPatientId("1L")).willReturn(patientAppointments);
        given(appointmentRepository.findAllByDoctorId("2L")).willReturn(doctorAppointments);

        // Act & Assert
        assertThrows(AppointmentConflictException.class, () -> {
            appointmentService.createAppointment(appointmentDTO);
        });
    }


}