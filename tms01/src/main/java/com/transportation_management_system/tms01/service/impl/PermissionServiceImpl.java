package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.permission.PermissionRequest;
import com.transportation_management_system.tms01.dto.permission.PermissionResponse;
import com.transportation_management_system.tms01.entity.auth.Permission;
import com.transportation_management_system.tms01.entity.auth.RolePermission;
import com.transportation_management_system.tms01.repository.auth.PermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RolePermissionRepository;
import com.transportation_management_system.tms01.service.auth.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByPermissionCode(request.getPermissionCode())) {
            throw new IllegalArgumentException("Mã quyền '" + request.getPermissionCode() + "' đã tồn tại trong hệ thống");
        }

        Permission permission = Permission.builder()
                .permissionCode(request.getPermissionCode())
                .permissionName(request.getPermissionName())
                .description(request.getDescription())
                .build();

        permission = permissionRepository.save(permission);
        return mapToResponse(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getPermissionsByRoleId(Long roleId) {
        List<RolePermission> list = rolePermissionRepository.findByRole_RoleId(roleId);
        return list.stream()
                .filter(rp -> rp.getPermission() != null)
                .map(rp -> mapToResponse(rp.getPermission()))
                .collect(Collectors.toList());
    }

    private PermissionResponse mapToResponse(Permission permission) {
        return PermissionResponse.builder()
                .permissionId(permission.getPermissionId())
                .permissionCode(permission.getPermissionCode())
                .permissionName(permission.getPermissionName())
                .description(permission.getDescription())
                .build();
    }
}
