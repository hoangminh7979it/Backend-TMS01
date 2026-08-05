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
public class RevenueReportDto {
    private String revenueCode;
    private String startDate;
    private String endDate;
    private Integer totalShipment;
    private BigDecimal totalExpense;
    private BigDecimal totalSalary;
    private BigDecimal revenueFinalCosts;
    private String createDate;
}
