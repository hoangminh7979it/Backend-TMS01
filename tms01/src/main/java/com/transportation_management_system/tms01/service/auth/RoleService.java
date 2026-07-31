package com.transportation_management_system.tms01.service.auth;

import com.transportation_management_system.tms01.dto.role.AssignPermissionsRequest;
import com.transportation_management_system.tms01.dto.role.RoleRequest;
import com.transportation_management_system.tms01.dto.role.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRole(Long id, RoleRequest request);

    void deleteRole(Long id);

    RoleResponse getRoleById(Long id);

    List<RoleResponse> getAllRoles();

    RoleResponse assignPermissionsToRole(AssignPermissionsRequest request);
}
