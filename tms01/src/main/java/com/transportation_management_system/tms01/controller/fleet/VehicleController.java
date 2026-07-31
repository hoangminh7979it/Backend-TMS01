package com.transportation_management_system.tms01.controller.fleet;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.fleet.VehicleRequest;
import com.transportation_management_system.tms01.dto.fleet.VehicleResponse;
import com.transportation_management_system.tms01.dto.fleet.VehicleTypeRequest;
import com.transportation_management_system.tms01.dto.fleet.VehicleTypeResponse;
import com.transportation_management_system.tms01.service.fleet.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("hasAuthority('VEHICLE_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Khai báo phương tiện mới thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VEHICLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getAllVehicles() {
        List<VehicleResponse> list = vehicleService.getAllVehicles();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách phương tiện thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@PathVariable Long id) {
        VehicleResponse response = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin phương tiện thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin phương tiện thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa hồ sơ phương tiện thành công"));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('VEHICLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getVehiclesByStatus(@PathVariable String status) {
        List<VehicleResponse> list = vehicleService.getVehiclesByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách phương tiện theo trạng thái thành công", list));
    }

    // --- VEHICLE TYPE APIS ---

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('VEHICLE_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<VehicleTypeResponse>>> getAllVehicleTypes() {
        List<VehicleTypeResponse> list = vehicleService.getAllVehicleTypes();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh mục loại phương tiện thành công", list));
    }

    @PostMapping("/types")
    @PreAuthorize("hasAuthority('VEHICLE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleTypeResponse>> createVehicleType(@Valid @RequestBody VehicleTypeRequest request) {
        VehicleTypeResponse response = vehicleService.createVehicleType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo loại phương tiện mới thành công", response));
    }

    @PutMapping("/types/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleTypeResponse>> updateVehicleType(
            @PathVariable Long id,
            @Valid @RequestBody VehicleTypeRequest request) {
        VehicleTypeResponse response = vehicleService.updateVehicleType(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật loại phương tiện thành công", response));
    }

    @DeleteMapping("/types/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVehicleType(@PathVariable Long id) {
        vehicleService.deleteVehicleType(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa loại phương tiện thành công"));
    }
}
