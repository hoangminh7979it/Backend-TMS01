package com.transportation_management_system.tms01.dto.revenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueSummaryResponse {

    private BigDecimal totalGrossRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal totalSalariesPaid;
    private BigDecimal netProfit;
    private Integer totalShipmentsCompleted;
    private Integer activeDriversCount;
}
