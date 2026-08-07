package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.permission.PermissionResponse;
import com.transportation_management_system.tms01.dto.role.AssignPermissionsRequest;
import com.transportation_management_system.tms01.dto.role.RoleRequest;
import com.transportation_management_system.tms01.dto.role.RoleResponse;
import com.transportation_management_system.tms01.entity.auth.Permission;
import com.transportation_management_system.tms01.entity.auth.Role;
import com.transportation_management_system.tms01.entity.auth.RolePermission;
import com.transportation_management_system.tms01.repository.auth.PermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RolePermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RoleRepository;
import com.transportation_management_system.tms01.service.auth.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new IllegalArgumentException("Mã vai trò '" + request.getRoleCode() + "' đã tồn tại trong hệ thống");
        }

        Role role = Role.builder()
                .roleCode(request.getRoleCode())
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();

        role = roleRepository.save(role);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            saveRolePermissions(role, request.getPermissionIds());
        }

        return getRoleById(role.getRoleId());
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với ID: " + id));

        if (request.getRoleName() != null) role.setRoleName(request.getRoleName());
        if (request.getDescription() != null) role.setDescription(request.getDescription());

        roleRepository.save(role);

        if (request.getPermissionIds() != null) {
            rolePermissionRepository.deleteByRole_RoleId(id);
            rolePermissionRepository.flush();
            saveRolePermissions(role, request.getPermissionIds());
        }


        return getRoleById(id);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với ID: " + id));

        rolePermissionRepository.deleteByRole_RoleId(id);
        roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với ID: " + id));

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRole_RoleId(id);
        List<PermissionResponse> permissionResponses = rolePermissions.stream()
                .filter(rp -> rp.getPermission() != null)
                .map(rp -> PermissionResponse.builder()
                        .permissionId(rp.getPermission().getPermissionId())
                        .permissionCode(rp.getPermission().getPermissionCode())
                        .permissionName(rp.getPermission().getPermissionName())
                        .description(rp.getPermission().getDescription())
                        .build())
                .collect(Collectors.toList());

        return RoleResponse.builder()
                .roleId(role.getRoleId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(permissionResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> getRoleById(role.getRoleId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleResponse assignPermissionsToRole(AssignPermissionsRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với ID: " + request.getRoleId()));

        rolePermissionRepository.deleteByRole_RoleId(role.getRoleId());
        rolePermissionRepository.flush();

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {

            saveRolePermissions(role, request.getPermissionIds());
        }

        return getRoleById(role.getRoleId());
    }

    private void saveRolePermissions(Role role, List<Long> permissionIds) {
        List<Permission> permissions = permissionRepository.findAllByPermissionIdIn(permissionIds);
        List<RolePermission> list = permissions.stream()
                .map(p -> RolePermission.builder()
                        .role(role)
                        .permission(p)
                        .build())
                .collect(Collectors.toList());
        rolePermissionRepository.saveAll(list);
    }
}
