package com.transportation_management_system.tms01.dto.hrm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeRequest {

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String employeeCode;

    @NotBlank(message = "Tên nhân viên không được để trống")
    private String firstname;

    private String lastname;
    private String nationalId;
    private String drivingLicenseId;
    private String address;
    private String email;
    private String phone;
    private Long employeeTypeId;
    private Long userId;
}
