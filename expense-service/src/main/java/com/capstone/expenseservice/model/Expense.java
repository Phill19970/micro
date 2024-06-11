package com.capstone.expenseservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.capstone.expenseservice.util.UpdateUtil.updateHelper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name of expense is required")
    private String name;
    private String category;
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount should be positive")
    private BigDecimal amount;

    @NotNull(message = "Date of expense is required")
    private LocalDate dateOfExpense;
    private Boolean paid;

    private String patientId;


    public void updateObject(Expense expense) {
        updateHelper(expense.getName(), this::setName);
        updateHelper(expense.getCategory(), this::setCategory);
        updateHelper(expense.getDescription(), this::setDescription);
        updateHelper(expense.getAmount(), this::setAmount);
        updateHelper(expense.getPaid(), this::setPaid);
    }


}
