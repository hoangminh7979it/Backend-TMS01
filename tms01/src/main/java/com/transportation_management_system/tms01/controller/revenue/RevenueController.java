package com.transportation_management_system.tms01.controller.revenue;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.revenue.*;
import com.transportation_management_system.tms01.service.revenue.RevenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/revenues")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('FINANCE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RevenueSummaryResponse>> getRevenueSummary() {
        RevenueSummaryResponse summary = revenueService.getRevenueSummary();
        return ResponseEntity.ok(ApiResponse.ok("L\u1EA5y ch\u1EC9 s\u1ED1 doanh thu & l\u1EE3i nhu\u1EADn r\u00F2ng th\u00E0nh c\u00F4ng", summary));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FINANCE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RevenueFinalResponse>>> getAllRevenueFinals() {
        List<RevenueFinalResponse> list = revenueService.getAllRevenueFinals();
        return ResponseEntity.ok(ApiResponse.ok("L\u1EA5y danh s\u00E1ch b\u00E1o c\u00E1o ch\u1ED1t doanh thu th\u00E0nh c\u00F4ng", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FINANCE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RevenueFinalResponse>> getRevenueFinalById(@PathVariable Long id) {
        RevenueFinalResponse response = revenueService.getRevenueFinalById(id);
        return ResponseEntity.ok(ApiResponse.ok("L\u1EA5y chi ti\u1EBFt b\u00E1o c\u00E1o doanh thu th\u00E0nh c\u00F4ng", response));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FINANCE_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RevenueFinalResponse>> createRevenueFinal(@Valid @RequestBody RevenueFinalRequest request) {
        RevenueFinalResponse response = revenueService.generateAndCreateRevenueFinal(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("L\u1EADp b\u00E1o c\u00E1o ch\u1ED1t doanh thu & l\u1EE3i nhu\u1EADn r\u00F2ng th\u00E0nh c\u00F4ng", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FINANCE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RevenueFinalResponse>> updateRevenueFinal(
            @PathVariable Long id,
            @Valid @RequestBody RevenueFinalRequest request) {
        RevenueFinalResponse response = revenueService.updateRevenueFinal(id, request);
        return ResponseEntity.ok(ApiResponse.ok("C\u1EADp nh\u1EADt b\u00E1o c\u00E1o doanh thu th\u00E0nh c\u00F4ng", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FINANCE_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRevenueFinal(@PathVariable Long id) {
        revenueService.deleteRevenueFinal(id);
        return ResponseEntity.ok(ApiResponse.ok("X\u00F3a b\u00E1o c\u00E1o doanh thu th\u00E0nh c\u00F4ng"));
    }
}
