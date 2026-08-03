package com.transportation_management_system.tms01.repository.payroll;

import com.transportation_management_system.tms01.entity.payroll.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    Optional<Salary> findBySalaryCodeAndIsDeleteFalse(String salaryCode);

    Optional<Salary> findBySalaryIdAndIsDeleteFalse(Long salaryId);

    List<Salary> findAllByIsDeleteFalse();

    List<Salary> findByEmployee_EmployeeIdAndIsDeleteFalse(Long employeeId);

    boolean existsBySalaryCode(String salaryCode);
}
