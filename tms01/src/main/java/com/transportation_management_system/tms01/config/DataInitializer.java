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
        initPermissions();
        initEmployeeTypes();
        initVehicleTypes();
        initStatusEnums();
        initExpenseTypes();
        initSampleCustomer();
        initSampleShipments();

        Role adminRole = roleRepository.findByRoleCode("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .roleCode("ADMIN")
                        .roleName("Quan Tri Vien He Thong")
                        .description("Quyen quan tri cao nhat he thong")
                        .build()));

        assignAllPermissionsToRole(adminRole);

        if (!userRepository.existsByUsername("admin")) {
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("Admin@6879"))
                    .firstname("Quan Tri")
                    .lastname("He Thong")
                    .email("admin@tms.com")
                    .phone("0987654321")
                    .isActive(true)
                    .workStartTime(LocalTime.of(0, 0))
                    .workEndTime(LocalTime.of(23, 59))
                    .role(adminRole)
                    .build();

            userRepository.save(adminUser);
            log.info(">>> Da khoi tao thanh cong tai khoan test: username='admin' | password='Admin@6879'");
        }
    }

    private void initExpenseTypes() {
        createExpenseTypeIfNotFound("FUEL", "Xang Dau Nhien Lieu", "Chi phi mua dau DO, xang xe tai va xe dau keo");
        createExpenseTypeIfNotFound("TOLL", "Phi Duong Bo & Cau Duong", "Chi phi mua ve tram BOT, cau duong va phi cao toc");
        createExpenseTypeIfNotFound("REPAIR", "Sua Chua & Bao Duong Xe", "Chi phi thay nhot, bao duong dinh ky va sua chua thay the phu tung");
        createExpenseTypeIfNotFound("PARKING", "Phi Ben Bai & Luu Dem", "Chi phi do xe, luu kho va dich vu boc xep ben bai");
        createExpenseTypeIfNotFound("POLICE", "Phi Su Co Tai Trong", "Chi phi xu ly su co giao thong va cau duong");
        createExpenseTypeIfNotFound("OTHER", "Chi Phi Khac", "Cac khoan chi phi phat sinh khac chua co trong danh muc");
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
        createStatusEnumIfNotFound("CREATED", "Moi Tao Don", "Don hang moi duoc tiep nhan chua gan xe");
        createStatusEnumIfNotFound("DISPATCHED", "Da Dieu Xe", "Da phan cong xe tai va tai xe phu trach");
        createStatusEnumIfNotFound("PICKED_UP", "Da Nhan Hang", "Tai xe da boc hang tai kho nhan va xuat phat");
        createStatusEnumIfNotFound("DELIVERED", "Da Giao Hang", "Hang hoa da duoc giao thanh cong toi noi nhan");
        createStatusEnumIfNotFound("CANCELLED", "Da Huy Don", "Don hang van chuyen da bi huy bo");
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
                    .firstname("Cong Ty TNHH Logistics")
                    .lastname("Toan Cau")
                    .companyName("Cong Ty TNHH Logistics Toan Cau (Global Freight)")
                    .taxCode("0101234567")
                    .email("contact@globalfreight.com.vn")
                    .phone("02439998888")
                    .address("Tang 8, Toa nha Viettel, Cau Giay, Ha Noi")
                    .customerType("CORPORATE")
                    .notes("Khach hang VIP ky hop dong cuoc van chuyen nam 2026")
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
                    .cargoType("Linh kien dien tu Samsung (12 Pallet, Thung carton)")
                    .receiptPlace("Kho KCN Yen Phong, Bac Ninh")
                    .deliveryPlace("Cang Dinh Vu, Hai Phong")
                    .weight(15.5)
                    .dateOfReceipt(LocalDateTime.now().minusDays(1))
                    .deliveryDate(LocalDateTime.now().plusDays(1))
                    .revenue(new BigDecimal("25000000"))
                    .incurredCosts(new BigDecimal("3500000"))
                    .notes("Hang dien tu cao cap, bao quan mui bat chang buoc chac chan")
                    .customer(customer)
                    .statusEnum(status)
                    .isDelete(false)
                    .createDate(LocalDateTime.now())
                    .build();

            shipmentRepository.save(s);
        }
    }

    private void initEmployeeTypes() {
        createEmployeeTypeIfNotFound("DRIVER", "Lai Xe Van Tai", "Doi ngu tai xe dieu khien xe tai va xe dau keo");
        createEmployeeTypeIfNotFound("CO_DRIVER", "Phu Xe / Boc Xep", "Ho tro boc do hang hoa va ap tai");
        createEmployeeTypeIfNotFound("DISPATCHER", "Dieu Do Xe", "Quan ly va sap xep lich trinh chay xe");
        createEmployeeTypeIfNotFound("MECHANIC", "Tho Ky Thuat / Bao Duong", "Bao duong va sua chua phuong tien");
        createEmployeeTypeIfNotFound("OFFICE", "Nhan Vien Van Phong / Ke Toan", "Khoi ho tro hanh chinh tai chinh");
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
        createVehicleTypeIfNotFound("TRUCK_LIGHT", "Xe Tai Nhe (1.5 - 3.5 Tan)", "Phu hop giao nhan noi thanh va kho ve tinh");
        createVehicleTypeIfNotFound("TRUCK_HEAVY", "Xe Tai Nang (8 - 15 Tan)", "Chuyen cho hang lien tinh tai trong lon");
        createVehicleTypeIfNotFound("CONTAINER_HEAD", "Xe Dau Keo Container", "Van chuyen container duong dai va Cang bien");
        createVehicleTypeIfNotFound("REFRIGERATED", "Xe Tai Lanh Specialized", "Van chuyen thuc pham va hang hoa dong lanh");
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
        createPermissionIfNotFound("SHIPMENT_READ", "Xem danh sach don hang van chuyen", "Quan ly Van Chuyen");
        createPermissionIfNotFound("SHIPMENT_CREATE", "Tao moi don hang van chuyen", "Quan ly Van Chuyen");
        createPermissionIfNotFound("SHIPMENT_UPDATE", "Cap nhat thong tin va trang thai don hang", "Quan ly Van Chuyen");
        createPermissionIfNotFound("SHIPMENT_DELETE", "Huy hoac xoa don hang van chuyen", "Quan ly Van Chuyen");

        createPermissionIfNotFound("CUSTOMER_READ", "Xem ho so khach hang va doi tac", "Quan ly Khach Hang");
        createPermissionIfNotFound("CUSTOMER_CREATE", "Them moi ho so khach hang", "Quan ly Khach Hang");
        createPermissionIfNotFound("CUSTOMER_UPDATE", "Cap nhat thong tin doi tac khach hang", "Quan ly Khach Hang");
        createPermissionIfNotFound("CUSTOMER_DELETE", "Xoa ho so khach hang", "Quan ly Khach Hang");

        createPermissionIfNotFound("EMPLOYEE_READ", "Xem ho so nhan su va tai xe", "Quan ly Nhan Su");
        createPermissionIfNotFound("EMPLOYEE_CREATE", "Them moi ho so nhan su", "Quan ly Nhan Su");
        createPermissionIfNotFound("EMPLOYEE_UPDATE", "Cap nhat thong tin nhan su va phan ca", "Quan ly Nhan Su");
        createPermissionIfNotFound("EMPLOYEE_DELETE", "Sa thai hoac xoa ho so nhan su", "Quan ly Nhan Su");

        createPermissionIfNotFound("EXPENSE_READ", "Xem danh sach phieu chi phi phat sinh", "Quan ly Chi Phi");
        createPermissionIfNotFound("EXPENSE_CREATE", "Tao phieu chi phi nhien lieu, cau duong", "Quan ly Chi Phi");
        createPermissionIfNotFound("EXPENSE_UPDATE", "Cap nhat va phe duyiet phieu chi phi", "Quan ly Chi Phi");
        createPermissionIfNotFound("EXPENSE_DELETE", "Huy hoac xoa phieu chi phi", "Quan ly Chi Phi");

        createPermissionIfNotFound("FINANCE_READ", "Xem bao cao doanh thu va bang luong", "Quan ly Tai Chinh");
        createPermissionIfNotFound("FINANCE_CREATE", "Lap ky tinh luong va chung tu thu chi", "Quan ly Tai Chinh");
        createPermissionIfNotFound("FINANCE_UPDATE", "Dieu chinh chot bang luong va doanh thu", "Quan ly Tai Chinh");
        createPermissionIfNotFound("FINANCE_DELETE", "Huy ky chung tu tai chinh", "Quan ly Tai Chinh");

        createPermissionIfNotFound("USER_READ", "Xem danh sach tai khoan nguoi dung", "Quan ly Nguoi Dung");
        createPermissionIfNotFound("USER_CREATE", "Tao moi tai khoan truy cap he thong", "Quan ly Nguoi Dung");
        createPermissionIfNotFound("USER_UPDATE", "Cap nhat tai khoan, khoa hoac reset mat khau", "Quan ly Nguoi Dung");
        createPermissionIfNotFound("USER_DELETE", "Xoa tai khoan nguoi dung", "Quan ly Nguoi Dung");

        createPermissionIfNotFound("ROLE_READ", "Xem ma tran phan quyen va danh sach vai tro", "Quan ly Phan Quyen");
        createPermissionIfNotFound("ROLE_CREATE", "Tao moi vai tro nguoi dung", "Quan ly Phan Quyen");
        createPermissionIfNotFound("ROLE_UPDATE", "Cap nhat va luu ma tran phan quyen", "Quan ly Phan Quyen");
        createPermissionIfNotFound("ROLE_DELETE", "Xoa vai tro khoi he thong", "Quan ly Phan Quyen");
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
