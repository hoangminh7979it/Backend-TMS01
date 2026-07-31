package com.transportation_management_system.tms01.dto.hrm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long employeeId;
    private String employeeCode;
    private String firstname;
    private String lastname;
    private String fullName;
    private String nationalId;
    private String drivingLicenseId;
    private String address;
    private String email;
    private String phone;
    
    private Long employeeTypeId;
    private String employeeTypeCode;
    private String employeeTypeName;

    private Long userId;
    private String username;

    private LocalDateTime createDate;
}
