package com.transportation_management_system.tms01.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {

    private String companyCode;

    @NotBlank(message = "Tên công ty không được để trống")
    private String name;

    private String taxCode;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private Long customerId;
}
