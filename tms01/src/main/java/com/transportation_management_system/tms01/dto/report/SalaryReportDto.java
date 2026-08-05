package com.transportation_management_system.tms01.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryReportDto {
    private String salaryCode;
    private String employeeCode;
    private String employeeName;
    private String drivingLicenseId;
    private String period;
    private BigDecimal salaryBasicCosts;
    private Integer totalShipmentCount;
    private BigDecimal totalSalaryPerShipment;
    private BigDecimal allowanceCosts;
    private BigDecimal deductionCosts;
    private BigDecimal salaryCosts;
}
