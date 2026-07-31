package com.transportation_management_system.tms01.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long customerId;
    private String customerCode;
    private String firstname;
    private String lastname;
    private String fullName;
    private String companyName;
    private String taxCode;
    private String email;
    private String phone;
    private String address;
    private String customerType;
    private String notes;
    private LocalDateTime createDate;
}
