package com.transportation_management_system.tms01.dto.fleet;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleRequest {

    private String vehicleCode;
    private String name;

    @NotBlank(message = "Biển số xe không được để trống")
    private String licensePlate;

    private Double payloadCapacity;
    private String status; // AVAILABLE, IN_TRANSIT, MAINTENANCE
    private LocalDate inspectionExpirationDate;
    private LocalDate insuranceExpirationDate;
    private Long employeeId; // Tài xế phụ trách
    private Long vehicleTypeId;
}
