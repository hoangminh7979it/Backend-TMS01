package com.transportation_management_system.tms01.repository.expense;

import com.transportation_management_system.tms01.entity.expense.ExpenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {

    List<ExpenseDetail> findByExpense_ExpenseId(Long expenseId);

    void deleteByExpense_ExpenseId(Long expenseId);
}
