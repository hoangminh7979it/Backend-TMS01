package com.transportation_management_system.tms01.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequest {

    @NotBlank(message = "Mã khách hàng không được để trống")
    private String customerCode;

    @NotBlank(message = "Họ tên / Tên đại diện không được để trống")
    private String firstname;

    private String lastname;
    private String companyName;
    private String taxCode;
    private String email;
    private String phone;
    private String address;
    private String customerType; // CORPORATE, INDIVIDUAL
    private String notes;
}
