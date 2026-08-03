package com.transportation_management_system.tms01.service.payroll;

import com.transportation_management_system.tms01.dto.payroll.*;

import java.util.List;

public interface SalaryService {

    SalaryResponse calculateAndCreateSalary(SalaryRequest request);

    SalaryResponse updateSalary(Long id, SalaryRequest request);

    SalaryResponse getSalaryById(Long id);

    List<SalaryResponse> getAllSalaries();

    void deleteSalary(Long id);
}
