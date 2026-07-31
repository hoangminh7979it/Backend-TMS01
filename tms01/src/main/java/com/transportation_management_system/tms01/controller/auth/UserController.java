package com.transportation_management_system.tms01.controller.auth;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.user.*;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import com.transportation_management_system.tms01.service.auth.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo tài khoản thành công với mật khẩu mặc định: Admin@6879", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> list = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách tài khoản thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin tài khoản thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật tài khoản thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa tài khoản thành công"));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        String username = authentication.getName();
        userService.changePassword(username, request);
        return ResponseEntity.ok(ApiResponse.ok("Đổi mật khẩu thành công"));
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasAuthority('USER_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.ok("Đặt lại mật khẩu thành công"));
    }

    @GetMapping("/{id}/check-active")
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkActive(@PathVariable Long id) {
        User user = userRepository.findByUserIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với ID: " + id));

        boolean active = userService.isUserActive(user);
        Map<String, Object> data = Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "isActive", active
        );
        return ResponseEntity.ok(ApiResponse.ok("Kiểm tra trạng thái kích hoạt thành công", data));
    }

    @GetMapping("/{id}/check-working-hours")
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkWorkingHours(@PathVariable Long id) {
        User user = userRepository.findByUserIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với ID: " + id));

        boolean inWorkingHours = userService.isWithinWorkingHours(user);
        Map<String, Object> data = Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "workStartTime", user.getWorkStartTime() != null ? user.getWorkStartTime().toString() : "24/7",
                "workEndTime", user.getWorkEndTime() != null ? user.getWorkEndTime().toString() : "24/7",
                "isWithinWorkingHours", inWorkingHours
        );
        return ResponseEntity.ok(ApiResponse.ok("Kiểm tra thời gian làm việc thành công", data));
    }
}
