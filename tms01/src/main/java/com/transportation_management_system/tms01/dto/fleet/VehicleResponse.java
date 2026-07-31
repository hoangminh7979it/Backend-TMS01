package com.transportation_management_system.tms01.dto.fleet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
    private Long id;
    private String vehicleCode;
    private String name;
    private String licensePlate;
    private Double payloadCapacity;
    private String status;
    private LocalDate inspectionExpirationDate;
    private LocalDate insuranceExpirationDate;

    // Driver info
    private Long employeeId;
    private String driverName;
    private String driverPhone;

    // Vehicle Type info
    private Long vehicleTypeId;
    private String vehicleTypeCode;
    private String vehicleTypeName;

    private LocalDateTime createDate;
}
