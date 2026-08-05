package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.systemfeature.SystemFeatureRequest;
import com.transportation_management_system.tms01.dto.systemfeature.SystemFeatureResponse;
import com.transportation_management_system.tms01.entity.auth.Permission;
import com.transportation_management_system.tms01.entity.auth.Role;
import com.transportation_management_system.tms01.entity.auth.RolePermission;
import com.transportation_management_system.tms01.entity.auth.SystemFeature;
import com.transportation_management_system.tms01.repository.auth.PermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RolePermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RoleRepository;
import com.transportation_management_system.tms01.repository.auth.SystemFeatureRepository;
import com.transportation_management_system.tms01.service.auth.SystemFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemFeatureServiceImpl implements SystemFeatureService {

    private final SystemFeatureRepository systemFeatureRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SystemFeatureResponse> getAllFeatures() {
        return systemFeatureRepository.findAllByOrderBySortOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemFeatureResponse> getActiveFeatures() {
        return systemFeatureRepository.findByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SystemFeatureResponse getFeatureById(Long featureId) {
        SystemFeature feature = systemFeatureRepository.findById(featureId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tính năng hệ thống với ID: " + featureId));
        return mapToResponse(feature);
    }

    @Override
    @Transactional
    public SystemFeatureResponse createFeature(SystemFeatureRequest request) {
        if (systemFeatureRepository.existsByFeatureCode(request.getFeatureCode())) {
            throw new IllegalArgumentException("Mã tính năng '" + request.getFeatureCode() + "' đã tồn tại");
        }

        String resGroup = (request.getResourceGroup() != null && !request.getResourceGroup().trim().isEmpty())
                ? request.getResourceGroup()
                : request.getFeatureName();

        SystemFeature feature = SystemFeature.builder()
                .featureCode(request.getFeatureCode().toUpperCase())
                .featureName(request.getFeatureName())
                .routePath(request.getRoutePath())
                .iconClass(request.getIconClass() != null ? request.getIconClass() : "fa-solid fa-cube")
                .resourceGroup(resGroup)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .description(request.getDescription())
                .build();

        feature = systemFeatureRepository.save(feature);

        // Auto-generate 4 CRUD Permissions for this new feature & assign to ADMIN role
        autoGeneratePermissionsForFeature(feature);

        return mapToResponse(feature);
    }

    @Override
    @Transactional
    public SystemFeatureResponse updateFeature(Long featureId, SystemFeatureRequest request) {
        SystemFeature feature = systemFeatureRepository.findById(featureId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tính năng hệ thống với ID: " + featureId));

        feature.setFeatureName(request.getFeatureName());
        feature.setRoutePath(request.getRoutePath());
        feature.setIconClass(request.getIconClass());
        if (request.getResourceGroup() != null && !request.getResourceGroup().trim().isEmpty()) {
            feature.setResourceGroup(request.getResourceGroup());
        }
        if (request.getSortOrder() != null) {
            feature.setSortOrder(request.getSortOrder());
        }
        if (request.getIsActive() != null) {
            feature.setIsActive(request.getIsActive());
        }
        feature.setDescription(request.getDescription());

        feature = systemFeatureRepository.save(feature);
        return mapToResponse(feature);
    }

    @Override
    @Transactional
    public void deleteFeature(Long featureId) {
        if (!systemFeatureRepository.existsById(featureId)) {
            throw new IllegalArgumentException("Không tìm thấy tính năng hệ thống với ID: " + featureId);
        }
        systemFeatureRepository.deleteById(featureId);
    }

    @Override
    @Transactional
    public SystemFeatureResponse toggleActiveStatus(Long featureId) {
        SystemFeature feature = systemFeatureRepository.findById(featureId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tính năng hệ thống với ID: " + featureId));

        feature.setIsActive(!Boolean.TRUE.equals(feature.getIsActive()));
        feature = systemFeatureRepository.save(feature);
        return mapToResponse(feature);
    }

    private void autoGeneratePermissionsForFeature(SystemFeature feature) {
        String baseCode = feature.getFeatureCode();
        String group = feature.getResourceGroup();

        createPermissionIfNotExist(baseCode + "_READ", "Xem danh sách " + feature.getFeatureName(), group, "READ");
        createPermissionIfNotExist(baseCode + "_CREATE", "Tạo mới " + feature.getFeatureName(), group, "CREATE");
        createPermissionIfNotExist(baseCode + "_UPDATE", "Chỉnh sửa " + feature.getFeatureName(), group, "UPDATE");
        createPermissionIfNotExist(baseCode + "_DELETE", "Xóa " + feature.getFeatureName(), group, "DELETE");
    }

    private void createPermissionIfNotExist(String code, String name, String group, String action) {
        Permission perm = permissionRepository.findByPermissionCode(code)
                .orElseGet(() -> permissionRepository.save(Permission.builder()
                        .permissionCode(code)
                        .permissionName(name)
                        .resourceGroup(group)
                        .actionType(action)
                        .description(name)
                        .build()));

        // Assign to ADMIN role automatically
        Optional<Role> adminRoleOpt = roleRepository.findByRoleCode("ADMIN");
        if (adminRoleOpt.isPresent()) {
            Role adminRole = adminRoleOpt.get();
            if (!rolePermissionRepository.existsByRole_RoleIdAndPermission_PermissionId(adminRole.getRoleId(), perm.getPermissionId())) {
                rolePermissionRepository.save(RolePermission.builder()
                        .role(adminRole)
                        .permission(perm)
                        .build());
            }
        }
    }

    private SystemFeatureResponse mapToResponse(SystemFeature feature) {
        return SystemFeatureResponse.builder()
                .featureId(feature.getFeatureId())
                .featureCode(feature.getFeatureCode())
                .featureName(feature.getFeatureName())
                .routePath(feature.getRoutePath())
                .iconClass(feature.getIconClass())
                .resourceGroup(feature.getResourceGroup())
                .sortOrder(feature.getSortOrder())
                .isActive(feature.getIsActive())
                .description(feature.getDescription())
                .build();
    }
}
