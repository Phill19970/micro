package com.capstone.expenseservice.service;

import com.capstone.expenseservice.client.UserClient;
import com.capstone.expenseservice.dto.v1.ExpenseDTO;
import com.capstone.expenseservice.exception.ResourceNotFoundException;
import com.capstone.expenseservice.mapper.ExpenseMapper;
import com.capstone.expenseservice.model.Expense;
import com.capstone.expenseservice.model.Patient;
import com.capstone.expenseservice.repository.ExpenseRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final UserClient userClient;


    @Override
    public List<Expense> getPatientExpenses(String patientId) {
        return expenseRepository.findAllByPatientId(
                patientId
        );
    }

    @Override
    public HttpStatus createExpense(String patientId, ExpenseDTO expenseDTO) {

        Patient patient;

        try{
            patient = userClient.getPatient(patientId);
        } catch (FeignException.NotFound e) {
            log.info("Creating expense, patient does not exist with id {}", patientId);
            throw new ResourceNotFoundException("Patient with id " + patientId + " does not exist");
        }

        Expense expense = expenseMapper.toEntity(expenseDTO);

        expense.setDateOfExpense(LocalDate.now());
        expense.setPaid(false);
        expense.setPatientId(patient.getId());


        expenseRepository.save(expense);
        log.info("Expense created {}", expense);

        return HttpStatus.CREATED;
    }

    @Override
    public Expense getExpense(UUID expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> {
                    log.info("Getting expense, expense does not exist with id {}", expenseId);
                    return new ResourceNotFoundException("Expense with id " + expenseId + " does not exist");
                });
    }

    @Override
    public HttpStatus updateExpense(UUID expenseId, Expense updatedExpense) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> {
                    log.info("Updating expense, expense does not exist with id {}", expenseId);
                    return new ResourceNotFoundException("Expense with id " + expenseId + " does not exist");
                });

        expense.updateObject(updatedExpense);
        expenseRepository.save(expense);
        log.info("Expense updated {}", expense);

        return HttpStatus.OK;
    }
}
