package com.transportation_management_system.tms01.controller.auth;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.permission.PermissionRequest;
import com.transportation_management_system.tms01.dto.permission.PermissionResponse;
import com.transportation_management_system.tms01.service.auth.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(@Valid @RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo quyền hạn mới thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<PermissionResponse> list = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh mục quyền hạn thành công", list));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<PermissionResponse> list = permissionService.getPermissionsByRoleId(roleId);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách quyền của vai trò thành công", list));
    }
}
