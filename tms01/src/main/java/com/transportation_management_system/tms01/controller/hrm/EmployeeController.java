package com.transportation_management_system.tms01.controller.hrm;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.hrm.EmployeeRequest;
import com.transportation_management_system.tms01.dto.hrm.EmployeeResponse;
import com.transportation_management_system.tms01.dto.hrm.EmployeeTypeRequest;
import com.transportation_management_system.tms01.dto.hrm.EmployeeTypeResponse;
import com.transportation_management_system.tms01.service.hrm.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo hồ sơ nhân viên thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> list = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách nhân viên thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin nhân viên thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin nhân viên thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa hồ sơ nhân viên thành công"));
    }

    // --- EMPLOYEE TYPE APIS ---

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmployeeTypeResponse>>> getAllEmployeeTypes() {
        List<EmployeeTypeResponse> list = employeeService.getAllEmployeeTypes();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh mục loại nhân viên thành công", list));
    }

    @PostMapping("/types")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeTypeResponse>> createEmployeeType(@Valid @RequestBody EmployeeTypeRequest request) {
        EmployeeTypeResponse response = employeeService.createEmployeeType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo loại nhân viên mới thành công", response));
    }

    @PutMapping("/types/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeTypeResponse>> updateEmployeeType(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeTypeRequest request) {
        EmployeeTypeResponse response = employeeService.updateEmployeeType(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật loại nhân viên thành công", response));
    }

    @DeleteMapping("/types/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployeeType(@PathVariable Long id) {
        employeeService.deleteEmployeeType(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa loại nhân viên thành công"));
    }

    @GetMapping("/type/{typeCode}")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployeesByTypeCode(@PathVariable String typeCode) {
        List<EmployeeResponse> list = employeeService.getEmployeesByTypeCode(typeCode);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách nhân viên theo loại thành công", list));
    }
}
