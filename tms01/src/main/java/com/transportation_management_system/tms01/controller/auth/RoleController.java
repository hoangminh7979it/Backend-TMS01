package com.transportation_management_system.tms01.controller.auth;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.role.AssignPermissionsRequest;
import com.transportation_management_system.tms01.dto.role.RoleRequest;
import com.transportation_management_system.tms01.dto.role.RoleResponse;
import com.transportation_management_system.tms01.service.auth.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo vai trò mới thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> list = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách vai trò thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable Long id) {
        RoleResponse response = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin vai trò thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.updateRole(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật vai trò thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa vai trò thành công"));
    }

    @PostMapping("/assign-permissions")
    @PreAuthorize("hasAuthority('ROLE_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> assignPermissions(@Valid @RequestBody AssignPermissionsRequest request) {
        RoleResponse response = roleService.assignPermissionsToRole(request);
        return ResponseEntity.ok(ApiResponse.ok("Gán quyền cho vai trò thành công", response));
    }
}
