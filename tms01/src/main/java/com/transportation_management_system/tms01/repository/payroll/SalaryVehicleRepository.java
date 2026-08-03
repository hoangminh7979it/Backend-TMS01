package com.transportation_management_system.tms01.repository.payroll;

import com.transportation_management_system.tms01.entity.payroll.SalaryVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryVehicleRepository extends JpaRepository<SalaryVehicle, Long> {

    List<SalaryVehicle> findBySalaryMain_SalaryId(Long salaryId);

    void deleteBySalaryMain_SalaryId(Long salaryId);
}
