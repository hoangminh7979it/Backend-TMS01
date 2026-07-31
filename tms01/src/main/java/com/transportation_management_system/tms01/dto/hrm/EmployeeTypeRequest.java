package com.transportation_management_system.tms01.dto.hrm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeTypeRequest {

    @NotBlank(message = "Mã loại nhân viên không được để trống")
    private String employeeTypeCode;

    @NotBlank(message = "Tên loại nhân viên không được để trống")
    private String employeeTypeName;

    private String description;
}
