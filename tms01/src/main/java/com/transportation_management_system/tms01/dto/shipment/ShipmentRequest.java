package com.transportation_management_system.tms01.dto.shipment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShipmentRequest {

    @NotBlank(message = "Mã đơn hàng không được để trống")
    private String shipmentCode;

    private Long customerId;
    private String cargoType;
    private String receiptPlace;
    private String deliveryPlace;
    private Double weight;
    private LocalDateTime dateOfReceipt;
    private LocalDateTime deliveryDate;
    private BigDecimal revenue;
    private BigDecimal incurredCosts;
    private Long vehicleId;
    private Long employeeId; // Tài xế phụ trách
    private Long statusEnumId;
    private String notes;
}
