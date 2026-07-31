package com.transportation_management_system.tms01.controller.customer;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.customer.CompanyRequest;
import com.transportation_management_system.tms01.dto.customer.CompanyResponse;
import com.transportation_management_system.tms01.dto.customer.CustomerRequest;
import com.transportation_management_system.tms01.dto.customer.CustomerResponse;
import com.transportation_management_system.tms01.service.customer.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAuthority('SHIPMENT_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Khai báo khách hàng mới thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> list = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách khách hàng thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin khách hàng thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin khách hàng thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa hồ sơ khách hàng thành công"));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getCustomersByType(@PathVariable String type) {
        List<CustomerResponse> list = customerService.getCustomersByType(type);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách khách hàng theo loại thành công", list));
    }

    // --- COMPANY APIS ---

    @GetMapping("/companies")
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        List<CompanyResponse> list = customerService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách công ty đối tác thành công", list));
    }

    @PostMapping("/companies")
    @PreAuthorize("hasAuthority('SHIPMENT_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(@Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = customerService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Khai báo công ty đối tác mới thành công", response));
    }

    @PutMapping("/companies/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = customerService.updateCompany(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin công ty đối tác thành công", response));
    }

    @DeleteMapping("/companies/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        customerService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa công ty đối tác thành công"));
    }
}
