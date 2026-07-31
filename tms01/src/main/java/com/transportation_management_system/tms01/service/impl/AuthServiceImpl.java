package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.auth.LoginRequest;
import com.transportation_management_system.tms01.dto.auth.LoginResponse;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import com.transportation_management_system.tms01.security.JwtTokenProvider;
import com.transportation_management_system.tms01.service.RedisService;
import com.transportation_management_system.tms01.service.auth.AuthService;
import com.transportation_management_system.tms01.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameAndIsDeleteFalse(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản hoặc mật khẩu không chính xác"));

        if (!userService.isUserActive(user)) {
            throw new IllegalStateException("Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt");
        }

        if (!userService.isWithinWorkingHours(user)) {
            throw new IllegalStateException("Tài khoản của bạn hiện đang ở ngoài ca/thời gian làm việc cho phép");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Tài khoản hoặc mật khẩu không chính xác");
        }

        String token = tokenProvider.generateToken(user.getUsername());

        // Lưu thông tin phiên đăng nhập người dùng vào Redis (Cache 24 giờ)
        redisService.save("USER_SESSION:" + user.getUsername(), token, 86400);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roleCode(user.getRole() != null ? user.getRole().getRoleCode() : null)
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .build();
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token != null && !token.isBlank()) {
            // Đưa token vào Blacklist trong Redis (Thời hạn 24 giờ)
            redisService.blacklistToken(token, 86400000);

            String username = tokenProvider.getUsernameFromToken(token);
            if (username != null) {
                redisService.delete("USER_SESSION:" + username);
            }
        }

        SecurityContextHolder.clearContext();
    }
}
