package com.transportation_management_system.tms01.dto.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponse {
    private Long permissionId;
    private String permissionCode;
    private String permissionName;
    private String description;
}
