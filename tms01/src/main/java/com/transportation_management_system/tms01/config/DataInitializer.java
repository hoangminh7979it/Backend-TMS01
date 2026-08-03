package com.transportation_management_system.tms01.config;

import com.transportation_management_system.tms01.entity.auth.Permission;
import com.transportation_management_system.tms01.entity.auth.Role;
import com.transportation_management_system.tms01.entity.auth.RolePermission;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.customer.Customer;
import com.transportation_management_system.tms01.entity.expense.ExpenseType;
import com.transportation_management_system.tms01.entity.fleet.VehicleType;
import com.transportation_management_system.tms01.entity.hrm.EmployeeType;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import com.transportation_management_system.tms01.repository.auth.PermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RolePermissionRepository;
import com.transportation_management_system.tms01.repository.auth.RoleRepository;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import com.transportation_management_system.tms01.repository.customer.CustomerRepository;
import com.transportation_management_system.tms01.repository.expense.ExpenseTypeRepository;
import com.transportation_management_system.tms01.repository.fleet.VehicleTypeRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeTypeRepository;
import com.transportation_management_system.tms01.repository.shipment.ShipmentRepository;
import com.transportation_management_system.tms01.repository.shipment.StatusEnumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final CustomerRepository customerRepository;
    private final ShipmentRepository shipmentRepository;
    private final StatusEnumRepository statusEnumRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Khởi tạo Danh mục Quyền Hạn Mẫu theo bộ chuẩn CRUD
        initPermissions();

        // 2. Khởi tạo Loại Nhân Viên Mẫu
        initEmployeeTypes();

        // 3. Khởi tạo Loại Phương Tiện Mẫu
        initVehicleTypes();

        // 4. Khởi tạo Trạng Thái Vận Chuyển Mẫu
        initStatusEnums();

        // 5. Khởi tạo Loại Chi Phí Mẫu
        initExpenseTypes();

        // 6. Khởi tạo Khách Hàng Mẫu KH-001
        initSampleCustomer();

        // 7. Khởi tạo Đơn Hàng Vận Chuyển Mẫu
        initSampleShipments();

        // 8. Khởi tạo Vai trò Mặc định ADMIN nếu chưa có
        Role adminRole = roleRepository.findByRoleCode("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .roleCode("ADMIN")
                        .roleName("Quản Trị Viên Hệ Thống")
                        .description("Quyền quản trị cao nhất hệ thống")
                        .build()));

        // 9. Gán tất cả Quyền hạn cho ADMIN nếu chưa gán
        assignAllPermissionsToRole(adminRole);

        // 10. Khởi tạo Tài khoản Mặc định 'admin' / 'Admin@6879' nếu chưa có
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

    private void initExpenseTypes() {
        createExpenseTypeIfNotFound("FUEL", "Xăng Dầu Nhiên Liệu", "Chi phí mua dầu DO, xăng xe tải và xe đầu kéo");
        createExpenseTypeIfNotFound("TOLL", "Phí Đường Bộ & Cầu Đường", "Chi phí mua vé trạm BOT, cầu đường và phí cao tốc");
        createExpenseTypeIfNotFound("REPAIR", "Sửa Chữa & Bảo Dưỡng Xe", "Chi phí thay nhớt, bảo dưỡng định kỳ và sửa chữa thay thế phụ tùng");
        createExpenseTypeIfNotFound("PARKING", "Phí Bến Bãi & Lưu Đêm", "Chi phí đỗ xe, lưu kho và dịch vụ bốc xếp bến bãi");
        createExpenseTypeIfNotFound("POLICE", "Phí Sự Cố Tải Trọng", "Chi phí xử lý sự cố giao thông và cầu đường");
        createExpenseTypeIfNotFound("OTHER", "Chi Phí Khác", "Các khoản chi phí phát sinh khác chưa có trong danh mục");
    }

    private void createExpenseTypeIfNotFound(String code, String name, String desc) {
        if (!expenseTypeRepository.existsByExpenseTypeCode(code)) {
            expenseTypeRepository.save(ExpenseType.builder()
                    .expenseTypeCode(code)
                    .expenseTypeName(name)
                    .description(desc)
                    .build());
        }
    }

    private void initStatusEnums() {
        createStatusEnumIfNotFound("CREATED", "Mới Tạo Đơn", "Đơn hàng mới được tiếp nhận chưa gán xe");
        createStatusEnumIfNotFound("DISPATCHED", "Đã Điều Xe", "Đã phân công xe tải và tài xế phụ trách");
        createStatusEnumIfNotFound("PICKED_UP", "Đã Nhận Hàng", "Tài xế đã bốc hàng tại kho nhận và xuất phát");
        createStatusEnumIfNotFound("DELIVERED", "Đã Giao Hàng", "Hàng hóa đã được giao thành công tới nơi nhận");
        createStatusEnumIfNotFound("CANCELLED", "Đã Hủy Đơn", "Đơn hàng vận chuyển đã bị hủy bỏ");
    }

    private void createStatusEnumIfNotFound(String code, String name, String desc) {
        if (!statusEnumRepository.existsByStatusEnumCode(code)) {
            statusEnumRepository.save(StatusEnum.builder()
                    .statusEnumCode(code)
                    .statusEnumName(name)
                    .description(desc)
                    .build());
        }
    }

    private void initSampleCustomer() {
        if (!customerRepository.existsByCustomerCode("KH-001")) {
            Customer c = Customer.builder()
                    .customerCode("KH-001")
                    .firstname("Công Ty TNHH Logistics")
                    .lastname("Toàn Cầu")
                    .companyName("Công Ty TNHH Logistics Toàn Cầu (Global Freight)")
                    .taxCode("0101234567")
                    .email("contact@globalfreight.com.vn")
                    .phone("02439998888")
                    .address("Tầng 8, Tòa nhà Viettel, Cầu Giấy, Hà Nội")
                    .customerType("CORPORATE")
                    .notes("Khách hàng VIP ký hợp đồng cước vận chuyển năm 2026")
                    .isDelete(false)
                    .createDate(LocalDateTime.now())
                    .build();

            customerRepository.save(c);
        }
    }

    private void initSampleShipments() {
        if (!shipmentRepository.existsByShipmentCode("DH-2026-001")) {
            Customer customer = customerRepository.findByCustomerCodeAndIsDeleteFalse("KH-001").orElse(null);
            StatusEnum status = statusEnumRepository.findByStatusEnumCode("PICKED_UP").orElse(null);

            Shipment s = Shipment.builder()
                    .shipmentCode("DH-2026-001")
                    .cargoType("Linh kiện điện tử Samsung (12 Pallet, Thùng carton)")
                    .receiptPlace("Kho KCN Yên Phong, Bắc Ninh")
                    .deliveryPlace("Cảng Đình Vũ, Hải Phòng")
                    .weight(15.5)
                    .dateOfReceipt(LocalDateTime.now().minusDays(1))
                    .deliveryDate(LocalDateTime.now().plusDays(1))
                    .revenue(new BigDecimal("25000000"))
                    .incurredCosts(new BigDecimal("3500000"))
                    .notes("Hàng điện tử cao cấp, bảo quản mui bạt chằng buộc chắc chắn")
                    .customer(customer)
                    .statusEnum(status)
                    .isDelete(false)
                    .createDate(LocalDateTime.now())
                    .build();

            shipmentRepository.save(s);
        }
    }

    private void initEmployeeTypes() {
        createEmployeeTypeIfNotFound("DRIVER", "Lái Xe Vận Tải", "Đội ngũ tài xế điều khiển xe tải và xe đầu kéo");
        createEmployeeTypeIfNotFound("CO_DRIVER", "Phụ Xe / Bốc Xếp", "Hỗ trợ bốc dỡ hàng hóa và áp tải");
        createEmployeeTypeIfNotFound("DISPATCHER", "Điều Độ Xe", "Quản lý và sắp xếp lịch trình chạy xe");
        createEmployeeTypeIfNotFound("MECHANIC", "Thợ Kỹ Thuật / Bảo Dưỡng", "Bảo dưỡng và sửa chữa phương tiện");
        createEmployeeTypeIfNotFound("OFFICE", "Nhân Viên Văn Phòng / Kế Toán", "Khối hỗ trợ hành chính tài chính");
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

    private void initVehicleTypes() {
        createVehicleTypeIfNotFound("TRUCK_LIGHT", "Xe Tải Nhẹ (1.5 - 3.5 Tấn)", "Phù hợp giao nhận nội thành và kho vệ tinh");
        createVehicleTypeIfNotFound("TRUCK_HEAVY", "Xe Tải Nặng (8 - 15 Tấn)", "Chuyên chở hàng liên tỉnh tải trọng lớn");
        createVehicleTypeIfNotFound("CONTAINER_HEAD", "Xe Đầu Kéo Container", "Vận chuyển container đường dài và Cảng biển");
        createVehicleTypeIfNotFound("REFRIGERATED", "Xe Tải Lạnh Specialized", "Vận chuyển thực phẩm và hàng hóa đông lạnh");
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

    private void initPermissions() {
        // 1. Quản lý Vận Chuyển / Đơn Hàng
        createPermissionIfNotFound("SHIPMENT_READ", "Xem danh sách đơn hàng vận chuyển", "Quản lý Vận Chuyển");
        createPermissionIfNotFound("SHIPMENT_CREATE", "Tạo mới đơn hàng vận chuyển", "Quản lý Vận Chuyển");
        createPermissionIfNotFound("SHIPMENT_UPDATE", "Cập nhật thông tin và trạng thái đơn hàng", "Quản lý Vận Chuyển");
        createPermissionIfNotFound("SHIPMENT_DELETE", "Hủy hoặc xóa đơn hàng vận chuyển", "Quản lý Vận Chuyển");

        // 2. Quản lý Khách Hàng
        createPermissionIfNotFound("CUSTOMER_READ", "Xem hồ sơ khách hàng và đối tác", "Quản lý Khách Hàng");
        createPermissionIfNotFound("CUSTOMER_CREATE", "Thêm mới hồ sơ khách hàng", "Quản lý Khách Hàng");
        createPermissionIfNotFound("CUSTOMER_UPDATE", "Cập nhật thông tin đối tác khách hàng", "Quản lý Khách Hàng");
        createPermissionIfNotFound("CUSTOMER_DELETE", "Xóa hồ sơ khách hàng", "Quản lý Khách Hàng");

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
