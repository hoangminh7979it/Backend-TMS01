package com.transportation_management_system.tms01.service.expense;

import com.transportation_management_system.tms01.dto.expense.*;

import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest request);

    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    ExpenseResponse getExpenseById(Long id);

    List<ExpenseResponse> getAllExpenses();

    void deleteExpense(Long id);

    // --- EXPENSE TYPES ---

    List<ExpenseTypeResponse> getAllExpenseTypes();

    ExpenseTypeResponse createExpenseType(ExpenseTypeRequest request);

    ExpenseTypeResponse updateExpenseType(Long id, ExpenseTypeRequest request);

    void deleteExpenseType(Long id);
}
