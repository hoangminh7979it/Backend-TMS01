package com.transportation_management_system.tms01.dto.payroll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryResponse {

    private Long salaryId;
    private String salaryCode;
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String employeeTypeName;
    private String drivingLicenseId;
    private List<String> licensePlates; // Tất cả biển số phương tiện trong kỳ lương
    private BigDecimal salaryBasicCosts;
    private Integer workDaysCount;
    private BigDecimal salaryBasicPerDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalShipmentCount;
    private BigDecimal driverShipmentRevenue;
    private BigDecimal tripSalaryPercentage;
    private BigDecimal totalSalaryPerShipment;
    private BigDecimal allowanceCosts;
    private BigDecimal deductionCosts;
    private BigDecimal salaryCosts;
    private String notes;
    private LocalDateTime createDate;

    private List<String> shipmentCodes;
}
