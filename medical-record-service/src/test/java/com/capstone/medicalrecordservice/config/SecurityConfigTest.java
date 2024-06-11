package com.capstone.medicalrecordservice.config;

import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional @Rollback
class SecurityConfigTest {

    static final String BASE_URL = "/api/v1/medical-record";

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    private MockMvc mockMvc;

    MedicalRecordDTO recordDTO;

    @MockBean
    MedicalRecordService medicalRecordService;



    @BeforeEach
    void setUp() {

        recordDTO = MedicalRecordDTO.builder()
                .roomNo("653")
                .doctorId("1L")
                .patientId("2L")
                .appointmentId(UUID.randomUUID())
                .checkInDate(LocalDate.now())
                .build();

    }


    @Test
    @WithMockUser(authorities = "PATIENT")
    public void testEndpointWhereRoleIsNotAccepted_IncorrectRole() throws Exception {

        given(medicalRecordService.createMedicalRecord(anyString(), any(MedicalRecordDTO.class)))
                .willReturn(HttpStatus.CREATED);

    //Test endpoint where expecting DOCTOR role so return forbidden
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    public void testEndpointWhereRoleIsAccepted() throws Exception {

        given(medicalRecordService.createMedicalRecord(anyString(), any(MedicalRecordDTO.class)))
                .willReturn(HttpStatus.CREATED);

        //Test endpoint where expecting DOCTOR role so return forbidden
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


}