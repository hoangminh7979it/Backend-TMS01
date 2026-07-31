package com.transportation_management_system.tms01.dto.fleet;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleTypeRequest {

    @NotBlank(message = "Mã loại xe không được để trống")
    private String vehicleTypeCode;

    @NotBlank(message = "Tên loại xe không được để trống")
    private String vehicleTypeName;

    private String description;
}
