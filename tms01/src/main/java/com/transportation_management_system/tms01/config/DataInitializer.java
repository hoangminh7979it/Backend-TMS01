package com.transportation_management_system.tms01.config;

import com.transportation_management_system.tms01.entity.auth.Role;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.repository.auth.RoleRepository;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Khởi tạo Vai trò Mặc định ADMIN nếu chưa có
        Role adminRole = roleRepository.findByRoleCode("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .roleCode("ADMIN")
                        .roleName("Quản Trị Viên Hệ Thống")
                        .description("Quyền quản trị cao nhất hệ thống")
                        .build()));

        // 2. Khởi tạo Tài khoản Mặc định 'admin' / 'Admin@6879' nếu chưa có
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("Admin@6879"))
                    .firstname("Quản Trị")
                    .lastname("Hệ Thống")
                    .email("admin@tms.com")
                    .phone("0987654321")
                    .isActive(true)
                    .workStartTime(LocalTime.of(0, 0))   // 00:00 - 23:59 (Truy cập 24/7)
                    .workEndTime(LocalTime.of(23, 59))
                    .role(adminRole)
                    .build();

            userRepository.save(adminUser);
            log.info(">>> Đã khởi tạo thành công tài khoản test: username='admin' | password='Admin@6879'");
        }
    }
}
