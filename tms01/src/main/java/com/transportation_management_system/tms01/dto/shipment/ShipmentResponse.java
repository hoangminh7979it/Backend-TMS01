package com.transportation_management_system.tms01.dto.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {
    private Long shipmentId;
    private String shipmentCode;
    private String cargoType;
    private String receiptPlace;
    private String deliveryPlace;
    private Double weight;
    private LocalDateTime dateOfReceipt;
    private LocalDateTime deliveryDate;
    private BigDecimal revenue;
    private BigDecimal incurredCosts;
    private BigDecimal netProfit; // Revenue - IncurredCosts
    private String notes;

    // Customer Info
    private Long customerId;
    private String customerName;
    private String customerPhone;

    // Vehicle Info
    private Long vehicleId;
    private String licensePlate;
    private String vehicleName;

    // Driver Info
    private Long employeeId;
    private String driverName;
    private String driverPhone;

    // Co-Driver Info
    private Long coDriverId;
    private String coDriverName;
    private String coDriverPhone;


    // Status Info
    private Long statusEnumId;
    private String statusEnumCode;
    private String statusEnumName;

    private LocalDateTime createDate;
}
