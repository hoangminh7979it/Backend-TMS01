package com.transportation_management_system.tms01.dto.payroll;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryRequest {

    @NotBlank(message = "M\u00E3 b\u1EA3ng l\u01B0\u01A1ng kh\u00F4ng \u0111\u01B0\u1EE3c \u0111\u1EC3 tr\u1ED1ng")
    private String salaryCode;

    private Long employeeId;
    private List<Long> vehicleIds; // Tất cả phương tiện từ chuyến hàng
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer workDaysCount;
    private BigDecimal salaryBasicPerDay;
    private Integer totalShipmentCount;
    private BigDecimal driverShipmentRevenue;
    private BigDecimal tripSalaryPercentage;
    private BigDecimal salaryBasicCosts;
    private BigDecimal totalSalaryPerShipment;
    private BigDecimal allowanceCosts;
    private BigDecimal deductionCosts;
    private BigDecimal salaryCosts;
    private String notes;

    private List<Long> shipmentIds;
}
