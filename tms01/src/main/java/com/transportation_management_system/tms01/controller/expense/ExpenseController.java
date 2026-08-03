package com.transportation_management_system.tms01.controller.expense;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.expense.*;
import com.transportation_management_system.tms01.service.expense.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @PreAuthorize("hasAuthority('EXPENSE_CREATE') or hasAuthority('SHIPMENT_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo mới phiếu chi phí vận tải thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EXPENSE_READ') or hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAllExpenses() {
        List<ExpenseResponse> list = expenseService.getAllExpenses();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách phiếu chi phí thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_READ') or hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(@PathVariable Long id) {
        ExpenseResponse response = expenseService.getExpenseById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy chi tiết phiếu chi phí thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_UPDATE') or hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin phiếu chi phí thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_DELETE') or hasAuthority('SHIPMENT_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa phiếu chi phí thành công"));
    }

    // --- EXPENSE TYPES APIS ---

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('EXPENSE_READ') or hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ExpenseTypeResponse>>> getAllExpenseTypes() {
        List<ExpenseTypeResponse> list = expenseService.getAllExpenseTypes();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh mục loại chi phí thành công", list));
    }

    @PostMapping("/types")
    @PreAuthorize("hasAuthority('EXPENSE_CREATE') or hasAuthority('SHIPMENT_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExpenseTypeResponse>> createExpenseType(@Valid @RequestBody ExpenseTypeRequest request) {
        ExpenseTypeResponse response = expenseService.createExpenseType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Khai báo loại chi phí mới thành công", response));
    }

    @PutMapping("/types/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_UPDATE') or hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExpenseTypeResponse>> updateExpenseType(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseTypeRequest request) {
        ExpenseTypeResponse response = expenseService.updateExpenseType(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật loại chi phí thành công", response));
    }

    @DeleteMapping("/types/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_DELETE') or hasAuthority('SHIPMENT_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteExpenseType(@PathVariable Long id) {
        expenseService.deleteExpenseType(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa loại chi phí thành công"));
    }
}
