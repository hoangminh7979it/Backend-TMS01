package com.transportation_management_system.tms01.dto.revenue;

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
public class RevenueFinalResponse {

    private Long revenueId;
    private String revenueCode;
    private String title;
    private Long vehicleId;
    private String licensePlate;
    private String vehicleName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalShipment;
    private BigDecimal grossRevenue;
    private BigDecimal totalExpense;
    private BigDecimal totalSalary;
    private BigDecimal revenueFinalCosts;
    private String notes;
    private LocalDateTime createDate;

    private List<String> shipmentCodes;
}
