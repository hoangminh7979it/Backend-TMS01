package com.transportation_management_system.tms01.dto.revenue;

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
public class RevenueFinalRequest {

    @NotBlank(message = "M\u00E3 b\u00E1o c\u00E1o doanh thu kh\u00F4ng \u0111\u01B0\u1EE3c \u0111\u1EC3 tr\u1ED1ng")
    private String revenueCode;

    private String title;
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalShipment;
    private BigDecimal grossRevenue;
    private BigDecimal totalExpense;
    private BigDecimal totalSalary;
    private BigDecimal revenueFinalCosts;
    private String notes;

    private List<Long> shipmentIds;
}
