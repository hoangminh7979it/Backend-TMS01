package com.transportation_management_system.tms01.repository.expense;

import com.transportation_management_system.tms01.entity.expense.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseCodeAndIsDeleteFalse(String expenseCode);

    Optional<Expense> findByExpenseIdAndIsDeleteFalse(Long expenseId);

    List<Expense> findAllByIsDeleteFalse();

    List<Expense> findByVehicle_IdAndIsDeleteFalse(Long vehicleId);

    List<Expense> findByShipment_ShipmentIdAndIsDeleteFalse(Long shipmentId);

    boolean existsByExpenseCode(String expenseCode);
}
