package com.transportation_management_system.tms01.service.hrm;

import com.transportation_management_system.tms01.dto.hrm.EmployeeRequest;
import com.transportation_management_system.tms01.dto.hrm.EmployeeResponse;
import com.transportation_management_system.tms01.dto.hrm.EmployeeTypeRequest;
import com.transportation_management_system.tms01.dto.hrm.EmployeeTypeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);

    EmployeeResponse getEmployeeById(Long id);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getEmployeesByTypeCode(String typeCode);

    List<EmployeeTypeResponse> getAllEmployeeTypes();

    EmployeeTypeResponse createEmployeeType(EmployeeTypeRequest request);

    EmployeeTypeResponse updateEmployeeType(Long id, EmployeeTypeRequest request);

    void deleteEmployeeType(Long id);
}
