package com.capstone.expenseservice.repository;

import com.capstone.expenseservice.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findAllByPatientId(String patientId);
}
