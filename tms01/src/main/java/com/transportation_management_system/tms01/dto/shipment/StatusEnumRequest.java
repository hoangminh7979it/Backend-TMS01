package com.transportation_management_system.tms01.dto.shipment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StatusEnumRequest {

    @NotBlank(message = "Mã trạng thái không được để trống")
    private String statusEnumCode;

    @NotBlank(message = "Tên trạng thái không được để trống")
    private String statusEnumName;

    private String description;
}
