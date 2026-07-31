package com.transportation_management_system.tms01.dto.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusEnumResponse {
    private Long statusEnumId;
    private String statusEnumCode;
    private String statusEnumName;
    private String description;
}
