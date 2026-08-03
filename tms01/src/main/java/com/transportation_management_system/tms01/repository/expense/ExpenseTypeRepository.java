package com.transportation_management_system.tms01.repository.expense;

import com.transportation_management_system.tms01.entity.expense.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {

    Optional<ExpenseType> findByExpenseTypeCode(String expenseTypeCode);

    boolean existsByExpenseTypeCode(String expenseTypeCode);
}
