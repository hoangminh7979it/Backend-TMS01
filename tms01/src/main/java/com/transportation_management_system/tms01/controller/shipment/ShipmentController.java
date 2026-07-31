package com.transportation_management_system.tms01.controller.shipment;

import com.transportation_management_system.tms01.dto.common.ApiResponse;
import com.transportation_management_system.tms01.dto.shipment.ShipmentRequest;
import com.transportation_management_system.tms01.dto.shipment.ShipmentResponse;
import com.transportation_management_system.tms01.dto.shipment.StatusEnumRequest;
import com.transportation_management_system.tms01.dto.shipment.StatusEnumResponse;
import com.transportation_management_system.tms01.service.shipment.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('SHIPMENT_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> createShipment(@Valid @RequestBody ShipmentRequest request) {
        ShipmentResponse response = shipmentService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo mới đơn hàng vận chuyển thành công", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ShipmentResponse>>> getAllShipments() {
        List<ShipmentResponse> list = shipmentService.getAllShipments();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách đơn hàng thành công", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentById(@PathVariable Long id) {
        ShipmentResponse response = shipmentService.getShipmentById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy chi tiết đơn hàng thành công", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> updateShipment(
            @PathVariable Long id,
            @Valid @RequestBody ShipmentRequest request) {
        ShipmentResponse response = shipmentService.updateShipment(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin đơn hàng thành công", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa đơn hàng thành công"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> updateShipmentStatus(
            @PathVariable Long id,
            @RequestParam String statusCode) {
        ShipmentResponse response = shipmentService.updateShipmentStatus(id, statusCode);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật trạng thái đơn hàng thành công", response));
    }

    @GetMapping("/status/{statusCode}")
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ShipmentResponse>>> getShipmentsByStatusCode(@PathVariable String statusCode) {
        List<ShipmentResponse> list = shipmentService.getShipmentsByStatusCode(statusCode);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách đơn hàng theo trạng thái thành công", list));
    }

    // --- STATUS ENUM APIS ---

    @GetMapping("/statuses")
    @PreAuthorize("hasAuthority('SHIPMENT_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StatusEnumResponse>>> getAllStatuses() {
        List<StatusEnumResponse> list = shipmentService.getAllStatuses();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh mục trạng thái vận chuyển thành công", list));
    }

    @PostMapping("/statuses")
    @PreAuthorize("hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StatusEnumResponse>> createStatus(@Valid @RequestBody StatusEnumRequest request) {
        StatusEnumResponse response = shipmentService.createStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo trạng thái mới thành công", response));
    }

    @PutMapping("/statuses/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StatusEnumResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusEnumRequest request) {
        StatusEnumResponse response = shipmentService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật trạng thái thành công", response));
    }

    @DeleteMapping("/statuses/{id}")
    @PreAuthorize("hasAuthority('SHIPMENT_DELETE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStatus(@PathVariable Long id) {
        shipmentService.deleteStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa trạng thái thành công"));
    }
}
