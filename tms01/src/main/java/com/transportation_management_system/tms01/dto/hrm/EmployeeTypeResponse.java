package com.transportation_management_system.tms01.dto.hrm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeTypeResponse {
    private Long employeeTypeId;
    private String employeeTypeCode;
    private String employeeTypeName;
    private String description;
}
