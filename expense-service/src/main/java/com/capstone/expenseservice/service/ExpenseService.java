package com.capstone.expenseservice.service;

import com.capstone.expenseservice.dto.v1.ExpenseDTO;
import com.capstone.expenseservice.model.Expense;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    List<Expense> getPatientExpenses(String patientId);

    HttpStatus createExpense(String patientId, ExpenseDTO expenseDTO);

    Expense getExpense(UUID expenseId);

    HttpStatus updateExpense(UUID expenseId, Expense updatedExpense);

}
