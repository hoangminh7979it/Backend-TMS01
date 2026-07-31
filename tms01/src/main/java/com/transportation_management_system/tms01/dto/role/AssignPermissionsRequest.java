package com.transportation_management_system.tms01.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignPermissionsRequest {

    @NotNull(message = "ID vai trò không được để trống")
    private Long roleId;

    private List<Long> permissionIds;
}
