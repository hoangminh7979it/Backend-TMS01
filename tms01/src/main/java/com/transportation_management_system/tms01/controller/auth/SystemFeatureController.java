package com.transportation_management_system.tms01.controller.auth;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.systemfeature.SystemFeatureRequest;
import com.transportation_management_system.tms01.dto.systemfeature.SystemFeatureResponse;
import com.transportation_management_system.tms01.service.auth.SystemFeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system-features")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SystemFeatureController {

    private final SystemFeatureService systemFeatureService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SystemFeatureResponse>>> getAllFeatures() {
        List<SystemFeatureResponse> list = systemFeatureService.getAllFeatures();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách tính năng hệ thống thành công", list));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SystemFeatureResponse>>> getActiveFeatures() {
        List<SystemFeatureResponse> list = systemFeatureService.getActiveFeatures();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách tính năng đang hoạt động thành công", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemFeatureResponse>> getFeatureById(@PathVariable Long id) {
        SystemFeatureResponse res = systemFeatureService.getFeatureById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy chi tiết tính năng thành công", res));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SystemFeatureResponse>> createFeature(@Valid @RequestBody SystemFeatureRequest request) {
        SystemFeatureResponse res = systemFeatureService.createFeature(request);
        return ResponseEntity.ok(ApiResponse.ok("Khai báo tính năng hệ thống mới thành công", res));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemFeatureResponse>> updateFeature(
            @PathVariable Long id,
            @Valid @RequestBody SystemFeatureRequest request
    ) {
        SystemFeatureResponse res = systemFeatureService.updateFeature(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật tính năng hệ thống thành công", res));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeature(@PathVariable Long id) {
        systemFeatureService.deleteFeature(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa tính năng hệ thống thành công", null));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse<SystemFeatureResponse>> toggleActiveStatus(@PathVariable Long id) {
        SystemFeatureResponse res = systemFeatureService.toggleActiveStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật trạng thái tính năng thành công", res));
    }
}
