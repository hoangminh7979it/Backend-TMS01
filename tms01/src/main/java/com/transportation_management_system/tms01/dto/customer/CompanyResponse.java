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
public class CompanyResponse {
    private Long companyId;
    private String companyCode;
    private String name;
    private String taxCode;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;

    private Long customerId;
    private String customerName;

    private LocalDateTime createDate;
}
