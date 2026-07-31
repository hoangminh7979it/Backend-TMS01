package com.transportation_management_system.tms01.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionRequest {

    @NotBlank(message = "Mã quyền hạn không được để trống")
    private String permissionCode;

    @NotBlank(message = "Tên quyền hạn không được để trống")
    private String permissionName;

    private String description;
}
