package com.transportation_management_system.tms01.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RoleRequest {

    @NotBlank(message = "Mã vai trò không được để trống")
    private String roleCode;

    @NotBlank(message = "Tên vai trò không được để trống")
    private String roleName;

    private String description;

    private List<Long> permissionIds;
}
