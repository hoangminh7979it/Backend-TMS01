package com.transportation_management_system.tms01.dto.fleet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleTypeResponse {
    private Long vehicleTypeId;
    private String vehicleTypeCode;
    private String vehicleTypeName;
    private String description;
}
