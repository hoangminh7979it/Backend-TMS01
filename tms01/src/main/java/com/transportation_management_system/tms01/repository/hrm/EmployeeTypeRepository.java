package com.transportation_management_system.tms01.repository.hrm;

import com.transportation_management_system.tms01.entity.hrm.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long> {

    Optional<EmployeeType> findByEmployeeTypeCode(String employeeTypeCode);

    boolean existsByEmployeeTypeCode(String employeeTypeCode);
}
