package com.transportation_management_system.tms01.service.fleet;

import com.transportation_management_system.tms01.dto.fleet.VehicleRequest;
import com.transportation_management_system.tms01.dto.fleet.VehicleResponse;
import com.transportation_management_system.tms01.dto.fleet.VehicleTypeRequest;
import com.transportation_management_system.tms01.dto.fleet.VehicleTypeResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse createVehicle(VehicleRequest request);

    VehicleResponse updateVehicle(Long id, VehicleRequest request);

    void deleteVehicle(Long id);

    VehicleResponse getVehicleById(Long id);

    List<VehicleResponse> getAllVehicles();

    List<VehicleResponse> getVehiclesByStatus(String status);

    List<VehicleResponse> getVehiclesByTypeCode(String typeCode);

    VehicleTypeResponse createVehicleType(VehicleTypeRequest request);

    VehicleTypeResponse updateVehicleType(Long id, VehicleTypeRequest request);

    void deleteVehicleType(Long id);

    List<VehicleTypeResponse> getAllVehicleTypes();
}
