package com.capstone.medicalrecordservice.controller;

import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.model.MedicalRecord;
import com.capstone.medicalrecordservice.service.MedicalRecordService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class MedicalRecordControllerTest {


    static final String BASE_URL = "/api/v1/medical-record";


    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MedicalRecordService medicalRecordService;

    MedicalRecord medicalRecord;
    MedicalRecordDTO medicalRecordDTO;

    @BeforeEach
    void setUp() {

        medicalRecord = MedicalRecord.builder()
                .checkInDate(LocalDate.now())
                .roomNo("5B")
                .build();

        medicalRecordDTO = MedicalRecordDTO.builder()
                .checkInDate(LocalDate.now())
                .appointmentId(UUID.randomUUID())
                .doctorId("1L")
                .patientId("1L")
                .roomNo("4B")
                .build();

    }

    @Test
    void getMedicalRecords() throws Exception {

        given(medicalRecordService.getMedicalRecords(anyString()))
                .willReturn(List.of(medicalRecord));

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL).param("patientId", "1L")
        );

        //then
        resultActions
                .andExpect(status().isOk());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        List<MedicalRecord> medicalRecords = objectMapper.readValue(content, new TypeReference<List<MedicalRecord>>() {
        });

        assertThat(medicalRecords).isEqualTo(List.of(medicalRecord));
    }

    @Test
    void getMedicalRecordsForPatient() throws Exception {

        given(medicalRecordService.getMedicalRecordsForAppointment(any(UUID.class)))
                .willReturn(medicalRecord);

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL + "/" + UUID.randomUUID())
        );

        //then
        resultActions
                .andExpect(status().isOk());

        String content = resultActions.andReturn().getResponse().getContentAsString();

        MedicalRecord record = objectMapper.readValue(content, MedicalRecord.class);

        assertThat(record).isEqualTo(medicalRecord);
    }

    @Test
    void createMedicalRecord() throws Exception{

        given(medicalRecordService.createMedicalRecord(anyString(), any(MedicalRecordDTO.class)))
                .willReturn(HttpStatus.CREATED);

        ResultActions resultActions = mockMvc.perform(
                post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordDTO))
        );

        //then
        resultActions
                .andExpect(status().isCreated());

    }
}