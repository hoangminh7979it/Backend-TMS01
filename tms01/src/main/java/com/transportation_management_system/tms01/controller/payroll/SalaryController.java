package com.transportation_management_system.tms01.controller.payroll;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.payroll.*;
import com.transportation_management_system.tms01.service.payroll.SalaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salaries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalaryController {

    private final SalaryService salaryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalaryResponse>>> getAllSalaries() {
        List<SalaryResponse> salaries = salaryService.getAllSalaries();
        return ResponseEntity.ok(ApiResponse.ok("L\u1EA5y danh s\u00E1ch b\u1EA3ng l\u01B0\u01A1ng th\u00E0nh c\u00F4ng", salaries));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalaryResponse>> getSalaryById(@PathVariable Long id) {
        SalaryResponse salary = salaryService.getSalaryById(id);
        return ResponseEntity.ok(ApiResponse.ok("L\u1EA5y th\u00F4ng tin b\u1EA3ng l\u01B0\u01A1ng th\u00E0nh c\u00F4ng", salary));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalaryResponse>> createSalary(@Valid @RequestBody SalaryRequest request) {
        SalaryResponse salary = salaryService.calculateAndCreateSalary(request);
        return ResponseEntity.ok(ApiResponse.ok("L\u1EADp phi\u1EBFu t\u00EDnh l\u01B0\u01A1ng nh\u00E2n s\u1EF1 th\u00E0nh c\u00F4ng", salary));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SalaryResponse>> updateSalary(@PathVariable Long id, @RequestBody SalaryRequest request) {
        SalaryResponse salary = salaryService.updateSalary(id, request);
        return ResponseEntity.ok(ApiResponse.ok("C\u1EADp nh\u1EADt b\u1EA3ng l\u01B0\u01A1ng th\u00E0nh c\u00F4ng", salary));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSalary(@PathVariable Long id) {
        salaryService.deleteSalary(id);
        return ResponseEntity.ok(ApiResponse.ok("X\u00F3a phi\u1EBFu l\u01B0\u01A1ng th\u00E0nh c\u00F4ng"));
    }
}
