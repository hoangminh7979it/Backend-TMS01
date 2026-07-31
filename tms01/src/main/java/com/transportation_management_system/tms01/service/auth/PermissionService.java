package com.transportation_management_system.tms01.service.auth;

import com.transportation_management_system.tms01.dto.permission.PermissionRequest;
import com.transportation_management_system.tms01.dto.permission.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    List<PermissionResponse> getPermissionsByRoleId(Long roleId);
}
