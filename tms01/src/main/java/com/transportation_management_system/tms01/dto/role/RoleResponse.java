package com.transportation_management_system.tms01.dto.role;

import com.transportation_management_system.tms01.dto.permission.PermissionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private Long roleId;
    private String roleCode;
    private String roleName;
    private String description;
    private List<PermissionResponse> permissions;
}
