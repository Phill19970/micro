package com.capstone.expenseservice.service;

import com.capstone.expenseservice.client.UserClient;
import com.capstone.expenseservice.dto.v1.ExpenseDTO;
import com.capstone.expenseservice.exception.ResourceNotFoundException;
import com.capstone.expenseservice.mapper.ExpenseMapper;
import com.capstone.expenseservice.mapper.ExpenseMapperImpl;
import com.capstone.expenseservice.model.Expense;
import com.capstone.expenseservice.model.Patient;
import com.capstone.expenseservice.repository.ExpenseRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    ExpenseRepository expenseRepository;
    @Mock
    UserClient userClient;

    ExpenseMapper expenseMapper = new ExpenseMapperImpl();
    ExpenseService expenseService;

    Patient testPatient;
    ExpenseDTO testExpenseDTO;
    Expense testExpense;

    Patient patient;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseMapper, userClient);

        testPatient = Patient.builder()
                .name("test")
                .email("test@email.com")
                .build();

        testExpenseDTO = ExpenseDTO.builder()
                .name("Test Expense")
                .category("Test")
                .amount(new BigDecimal("100.00"))
                .build();

        testExpense = Expense.builder()
                .name("Expense")
                .build();

        patient = Patient.builder()
                .build();
    }

    @Test
    void getPatientExpenses() {
        given(expenseRepository.findAllByPatientId(anyString())).willReturn(Arrays.asList(testExpense));

        //when
        List<Expense> patientExpenses = expenseService.getPatientExpenses("1L");

        //then
        then(expenseRepository).should(times(1)).findAllByPatientId("1L");
        assertThat(patientExpenses).isEqualTo(Arrays.asList(testExpense));
    }


    @Test
    void createExpense() {
        given(userClient.getPatient(anyString()))
                .willReturn(patient);

        //when
        HttpStatus httpStatus = expenseService.createExpense("1L", testExpenseDTO);


        then(userClient).should(times(1)).getPatient("1L");
        then(expenseRepository).should(times(1)).save(any(Expense.class));
        assertThat(httpStatus).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createExpense_GivenWrongId_ThrowResourceError() {
        //given
        given(userClient.getPatient(anyString()))
                .willThrow(FeignException.NotFound.class);


        //then
        assertThrows(ResourceNotFoundException.class, () -> expenseService.createExpense("1L", testExpenseDTO));
        then(userClient).should().getPatient("1L");
    }

    @Test
    void getExpense() {
        given(expenseRepository.findById(any(UUID.class))).willReturn(Optional.of(testExpense));

        Expense expense = expenseService.getExpense(UUID.randomUUID());

        then(expenseRepository).should(times(1)).findById(any(UUID.class));
        assertThat(expense).isEqualTo(testExpense);
    }

    @Test
    void getExpense_GivenWrongId_ThrowResourceError() {
        //given
        given(expenseRepository.findById(any(UUID.class))).willThrow(ResourceNotFoundException.class);


        //then
        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpense(UUID.randomUUID()));
        then(expenseRepository).should(times(1)).findById(any(UUID.class));
    }

    @Test
    void updateExpense() {
        Expense updateExpense = Expense.builder().build();

        given(expenseRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(testExpense));

        HttpStatus httpStatus = expenseService.updateExpense(UUID.randomUUID(), updateExpense);

        then(expenseRepository).should(times(1)).save(any(Expense.class));
        then(expenseRepository).should(times(1)).findById(any(UUID.class));
        assertThat(httpStatus).isEqualTo(HttpStatus.OK);
    }
}