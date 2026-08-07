package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.user.*;
import com.transportation_management_system.tms01.entity.auth.Role;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.repository.auth.RoleRepository;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import com.transportation_management_system.tms01.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập '" + request.getUsername() + "' đã tồn tại trong hệ thống");
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty() && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' đã được sử dụng");
        }

        Role role = null;
        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với ID: " + request.getRoleId()));
        }

        String rawPassword = (request.getPassword() != null && !request.getPassword().isBlank())
                ? request.getPassword()
                : DEFAULT_PASSWORD;
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .isActive(true)
                .workStartTime(request.getWorkStartTime())
                .workEndTime(request.getWorkEndTime())
                .build();


        user = userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findByUserIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với ID: " + id));

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email '" + request.getEmail() + "' đã được sử dụng");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFirstname() != null) user.setFirstname(request.getFirstname());
        if (request.getLastname() != null) user.setLastname(request.getLastname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getIsActive() != null) user.setIsActive(request.getIsActive());

        if (request.getWorkStartTime() != null) user.setWorkStartTime(request.getWorkStartTime());
        if (request.getWorkEndTime() != null) user.setWorkEndTime(request.getWorkEndTime());

        // Cập nhật mật khẩu mới nếu được cung cấp
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getNewPassword().length() < 4) {
                throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 4 ký tự");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với ID: " + request.getRoleId()));
            user.setRole(role);
        }

        user = userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findByUserIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với ID: " + id));

        user.setIsDelete(true);
        user.setDeleteDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findByUserIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với ID: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsernameAndIsDeleteFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng: " + username));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới và mật khẩu xác nhận không trùng khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByUserIdAndIsDeleteFalse(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với ID: " + request.getUserId()));

        String newPassword = (request.getCustomNewPassword() != null && !request.getCustomNewPassword().isBlank())
                ? request.getCustomNewPassword()
                : DEFAULT_PASSWORD;

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean isUserActive(User user) {
        if (user == null) return false;
        return Boolean.TRUE.equals(user.getIsActive()) && !Boolean.TRUE.equals(user.getIsDelete());
    }

    @Override
    public boolean isWithinWorkingHours(User user) {
        if (user == null) return false;
        LocalTime startTime = user.getWorkStartTime();
        LocalTime endTime = user.getWorkEndTime();

        if (startTime == null || endTime == null) {
            return true;
        }

        LocalTime now = LocalTime.now();
        if (startTime.isBefore(endTime)) {
            return !now.isBefore(startTime) && !now.isAfter(endTime);
        } else {
            return !now.isBefore(startTime) || !now.isAfter(endTime);
        }
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())
                .workStartTime(user.getWorkStartTime())
                .workEndTime(user.getWorkEndTime())
                .roleId(user.getRole() != null ? user.getRole().getRoleId() : null)
                .roleCode(user.getRole() != null ? user.getRole().getRoleCode() : null)
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .createDate(user.getCreateDate())
                .build();
    }
}
