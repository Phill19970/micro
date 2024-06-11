package com.capstone.expenseservice.controller;

import com.capstone.expenseservice.dto.v1.ExpenseDTO;
import com.capstone.expenseservice.model.Expense;
import com.capstone.expenseservice.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getPatientExpenses(@RequestParam(required = true) String patientId) {
        return expenseService.getPatientExpenses(patientId);
    }

    @PostMapping("/{patientId}")
    @PreAuthorize("hasAuthority('DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> createExpense(
            @PathVariable String patientId,
            @Valid @RequestBody ExpenseDTO expenseDTO
    ) {
        return ResponseEntity
                .status(expenseService.createExpense(patientId, expenseDTO))
                .build();
    }

    @GetMapping("/{expenseId}")
    public Expense getExpense(@PathVariable UUID expenseId) {
        return expenseService.getExpense(expenseId);
    }

    @PutMapping("/{expenseId}")
    @PreAuthorize("hasAuthority('DOCTOR')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> updateExpense(
            @PathVariable UUID expenseId,
            @RequestBody Expense expense
    ) {
        return ResponseEntity
                .status(expenseService.updateExpense(expenseId, expense))
                .build();
    }

}
