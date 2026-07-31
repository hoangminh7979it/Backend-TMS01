package com.transportation_management_system.tms01.repository.hrm;

import com.transportation_management_system.tms01.entity.hrm.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCodeAndIsDeleteFalse(String employeeCode);

    Optional<Employee> findByEmployeeIdAndIsDeleteFalse(Long employeeId);

    List<Employee> findAllByIsDeleteFalse();

    List<Employee> findByEmployeeType_EmployeeTypeCodeAndIsDeleteFalse(String typeCode);

    boolean existsByEmployeeCode(String employeeCode);
}
