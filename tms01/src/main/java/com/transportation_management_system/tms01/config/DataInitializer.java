package com.transportation_management_system.tms01.config;

import com.transportation_management_system.tms01.entity.auth.Permission;
import com.transportation_management_system.tms01.entity.auth.Role;
import com.transportation_management_system.tms01.entity.auth.RolePermission;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.fleet.VehicleType;
import com.transportation_management_system.tms01.entity.hrm.EmployeeType;
import com.transportation_management_system.tms01.repository.auth.PermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RolePermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RoleRepository;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import com.transportation_management_system.tms01.repository.fleet.VehicleTypeRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Khởi tạo Danh mục Quyền Hạn Mẫu theo bộ chuẩn CRUD (READ, CREATE, UPDATE, DELETE)
        initPermissions();

        // 2. Khởi tạo Loại Nhân Viên Mẫu
        initEmployeeTypes();

        // 3. Khởi tạo Loại Phương Tiện Mẫu
        initVehicleTypes();

        // 4. Khởi tạo Vai trò Mặc định ADMIN nếu chưa có
        Role adminRole = roleRepository.findByRoleCode("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .roleCode("ADMIN")
                        .roleName("Quản Trị Viên Hệ Thống")
                        .description("Quyền quản trị cao nhất hệ thống")
                        .build()));

        // 5. Gán tất cả Quyền hạn cho ADMIN nếu chưa gán
        assignAllPermissionsToRole(adminRole);

        // 6. Khởi tạo Tài khoản Mặc định 'admin' / 'Admin@6879' nếu chưa có
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

    private void initVehicleTypes() {
        createVehicleTypeIfNotFound("TRUCK_HEAD", "Xe Đầu Kéo Container", "Xe đầu kéo chuyên chở Rơ-moóc & Container 40ft/20ft");
        createVehicleTypeIfNotFound("TRUCK_15T", "Xe Tải Heavy 15 Tấn", "Xe tải thùng kín / mui bạt 15 Tấn đường dài");
        createVehicleTypeIfNotFound("TRUCK_8T", "Xe Tải Medium 8 Tấn", "Xe tải liên tỉnh 8 Tấn");
        createVehicleTypeIfNotFound("TRUCK_3.5T", "Xe Tải Light 3.5 Tấn", "Xe tải nội thành 3.5 Tấn");
        createVehicleTypeIfNotFound("VAN", "Xe Tải Van Giao Hàng Nhanh", "Xe tải van 1 Tấn chạy giờ cao điểm thành phố");
    }

    private void createVehicleTypeIfNotFound(String code, String name, String desc) {
        if (!vehicleTypeRepository.existsByVehicleTypeCode(code)) {
            vehicleTypeRepository.save(VehicleType.builder()
                    .vehicleTypeCode(code)
                    .vehicleTypeName(name)
                    .description(desc)
                    .build());
        }
    }

    private void initEmployeeTypes() {
        createEmployeeTypeIfNotFound("DRIVER", "Tài Xế Vận Tải", "Lái xe đầu kéo, xe tải đường dài");
        createEmployeeTypeIfNotFound("CO_DRIVER", "Phụ Xe", "Phụ xe giao nhận hàng hóa");
        createEmployeeTypeIfNotFound("COORDINATOR", "Điều Hành Vận Tải", "Điều phối xe và điều động chuyến hàng");
        createEmployeeTypeIfNotFound("MECHANIC", "Kỹ Thuật / Bảo Dưỡng", "Sửa chữa, bảo dưỡng phương tiện");
        createEmployeeTypeIfNotFound("ACCOUNTANT", "Kế Toán Vận Tải", "Kế toán chi phí, xăng dầu, lương chuyến");
        createEmployeeTypeIfNotFound("OFFICER", "Nhân Viên Văn Phòng", "Nhân sự văn phòng và hành chính");
    }

    private void createEmployeeTypeIfNotFound(String code, String name, String desc) {
        if (!employeeTypeRepository.existsByEmployeeTypeCode(code)) {
            employeeTypeRepository.save(EmployeeType.builder()
                    .employeeTypeCode(code)
                    .employeeTypeName(name)
                    .description(desc)
                    .build());
        }
    }

    private void initPermissions() {
        // 1. Quản lý Đơn Hàng
        createPermissionIfNotFound("SHIPMENT_READ", "Xem danh sách và chi tiết đơn hàng", "Quản lý Đơn Hàng");
        createPermissionIfNotFound("SHIPMENT_CREATE", "Tạo mới đơn hàng vận chuyển", "Quản lý Đơn Hàng");
        createPermissionIfNotFound("SHIPMENT_UPDATE", "Cập nhật tiến độ và lộ trình đơn hàng", "Quản lý Đơn Hàng");
        createPermissionIfNotFound("SHIPMENT_DELETE", "Hủy hoặc xóa đơn hàng", "Quản lý Đơn Hàng");

        // 2. Quản lý Đội Xe
        createPermissionIfNotFound("VEHICLE_READ", "Xem danh sách phương tiện và hạn đăng kiểm", "Quản lý Đội Xe");
        createPermissionIfNotFound("VEHICLE_CREATE", "Khai báo phương tiện vận tải mới", "Quản lý Đội Xe");
        createPermissionIfNotFound("VEHICLE_UPDATE", "Cập nhật thông tin xe và lịch bảo dưỡng", "Quản lý Đội Xe");
        createPermissionIfNotFound("VEHICLE_DELETE", "Xóa hoặc ngưng vận hành phương tiện", "Quản lý Đội Xe");

        // 3. Quản lý Nhân Sự
        createPermissionIfNotFound("EMPLOYEE_READ", "Xem hồ sơ nhân sự và tài xế", "Quản lý Nhân Sự");
        createPermissionIfNotFound("EMPLOYEE_CREATE", "Thêm mới hồ sơ nhân sự", "Quản lý Nhân Sự");
        createPermissionIfNotFound("EMPLOYEE_UPDATE", "Cập nhật thông tin nhân sự và phân ca", "Quản lý Nhân Sự");
        createPermissionIfNotFound("EMPLOYEE_DELETE", "Sa thải hoặc xóa hồ sơ nhân sự", "Quản lý Nhân Sự");

        // 4. Quản lý Chi Phí
        createPermissionIfNotFound("EXPENSE_READ", "Xem danh sách phiếu chi phí phát sinh", "Quản lý Chi Phí");
        createPermissionIfNotFound("EXPENSE_CREATE", "Tạo phiếu chi phí nhiên liệu, cầu đường", "Quản lý Chi Phí");
        createPermissionIfNotFound("EXPENSE_UPDATE", "Cập nhật và phê duyệt phiếu chi phí", "Quản lý Chi Phí");
        createPermissionIfNotFound("EXPENSE_DELETE", "Hủy hoặc xóa phiếu chi phí", "Quản lý Chi Phí");

        // 5. Quản lý Tài Chính
        createPermissionIfNotFound("FINANCE_READ", "Xem báo cáo doanh thu và bảng lương", "Quản lý Tài Chính");
        createPermissionIfNotFound("FINANCE_CREATE", "Lập kỳ tính lương và chứng từ thu chi", "Quản lý Tài Chính");
        createPermissionIfNotFound("FINANCE_UPDATE", "Điều chỉnh chốt bảng lương và doanh thu", "Quản lý Tài Chính");
        createPermissionIfNotFound("FINANCE_DELETE", "Hủy kỳ chứng từ tài chính", "Quản lý Tài Chính");

        // 6. Quản lý Người Dùng
        createPermissionIfNotFound("USER_READ", "Xem danh sách tài khoản người dùng", "Quản lý Người Dùng");
        createPermissionIfNotFound("USER_CREATE", "Tạo mới tài khoản truy cập hệ thống", "Quản lý Người Dùng");
        createPermissionIfNotFound("USER_UPDATE", "Cập nhật tài khoản, khóa hoặc reset mật khẩu", "Quản lý Người Dùng");
        createPermissionIfNotFound("USER_DELETE", "Xóa tài khoản người dùng", "Quản lý Người Dùng");

        // 7. Quản lý Phân Quyền
        createPermissionIfNotFound("ROLE_READ", "Xem ma trận phân quyền và danh sách vai trò", "Quản lý Phân Quyền");
        createPermissionIfNotFound("ROLE_CREATE", "Tạo mới vai trò người dùng", "Quản lý Phân Quyền");
        createPermissionIfNotFound("ROLE_UPDATE", "Cập nhật và lưu ma trận phân quyền", "Quản lý Phân Quyền");
        createPermissionIfNotFound("ROLE_DELETE", "Xóa vai trò khỏi hệ thống", "Quản lý Phân Quyền");
    }

    private void createPermissionIfNotFound(String code, String name, String desc) {
        if (!permissionRepository.existsByPermissionCode(code)) {
            permissionRepository.save(Permission.builder()
                    .permissionCode(code)
                    .permissionName(name)
                    .description(desc)
                    .build());
        }
    }

    private void assignAllPermissionsToRole(Role role) {
        List<Permission> allPermissions = permissionRepository.findAll();
        for (Permission p : allPermissions) {
            if (!rolePermissionRepository.existsByRole_RoleIdAndPermission_PermissionId(role.getRoleId(), p.getPermissionId())) {
                rolePermissionRepository.save(RolePermission.builder()
                        .role(role)
                        .permission(p)
                        .build());
            }
        }
    }
}
