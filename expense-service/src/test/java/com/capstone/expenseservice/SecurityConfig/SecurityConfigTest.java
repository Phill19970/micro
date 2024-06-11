package com.capstone.expenseservice.SecurityConfig;

import com.capstone.expenseservice.dto.v1.ExpenseDTO;
import com.capstone.expenseservice.model.Expense;
import com.capstone.expenseservice.service.ExpenseService;
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

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional @Rollback
class SecurityConfigTest {

    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ExpenseService expenseService;

    ExpenseDTO expenseDTO;

    Expense expense;

    @BeforeEach
    void setUp() {


        expenseDTO = ExpenseDTO.builder()
                .category("Category")
                .description("Cat Description")
                .name("Expense")
                .amount(BigDecimal.valueOf(123L))
                .build();

        expense = Expense.builder()
                .category("Category")
                .description("Cat Description")
                .name("Expense")
                .build();

    }




    @Test
    @WithMockUser(authorities = "PATIENT")
    public void testEndpointWhereRoleIsNotAccepted_IncorrectRole() throws Exception {
    //Test endpoint where expecting DOCTOR role so return forbidden
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    public void testEndpointWhereRoleAccepted_PostExpense() throws Exception {

        given(expenseService.createExpense(anyString(), any(ExpenseDTO.class)))
                .willReturn(HttpStatus.CREATED);


    //Test endpoint where expecting DOCTOR role so return forbidden
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    public void testEndpointWhereRoleAccepted_PutExpense() throws Exception {

        given(expenseService.updateExpense(any(UUID.class), any(Expense.class)))
                .willReturn(HttpStatus.OK);


        //Test endpoint where expecting DOCTOR role so return forbidden
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/expenses/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "PATIENT")
    public void testEndpointWhereRoleAccepted() throws Exception {

        given(expenseService.updateExpense(any(UUID.class), any(Expense.class)))
                .willReturn(HttpStatus.OK);


        //Test endpoint where expecting DOCTOR role so return forbidden
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/expenses/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}