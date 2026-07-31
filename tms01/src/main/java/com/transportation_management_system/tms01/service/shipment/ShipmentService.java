package com.transportation_management_system.tms01.service.shipment;

import com.transportation_management_system.tms01.dto.shipment.ShipmentRequest;
import com.transportation_management_system.tms01.dto.shipment.ShipmentResponse;
import com.transportation_management_system.tms01.dto.shipment.StatusEnumRequest;
import com.transportation_management_system.tms01.dto.shipment.StatusEnumResponse;

import java.util.List;

public interface ShipmentService {

    ShipmentResponse createShipment(ShipmentRequest request);

    ShipmentResponse updateShipment(Long id, ShipmentRequest request);

    void deleteShipment(Long id);

    ShipmentResponse getShipmentById(Long id);

    List<ShipmentResponse> getAllShipments();

    List<ShipmentResponse> getShipmentsByStatusCode(String statusCode);

    ShipmentResponse updateShipmentStatus(Long id, String statusCode);

    List<StatusEnumResponse> getAllStatuses();

    StatusEnumResponse createStatus(StatusEnumRequest request);

    StatusEnumResponse updateStatus(Long id, StatusEnumRequest request);

    void deleteStatus(Long id);
}
