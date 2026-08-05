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
public class ShipmentReportDto {
    private String shipmentCode;
    private String customerName;
    private String receiptPlace;
    private String deliveryPlace;
    private Double weight;
    private String dateOfReceipt;
    private String deliveryDate;
    private BigDecimal revenue;
    private BigDecimal incurredCosts;
    private String statusName;
}
