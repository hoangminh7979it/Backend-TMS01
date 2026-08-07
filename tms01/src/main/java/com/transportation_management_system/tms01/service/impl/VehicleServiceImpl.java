package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.fleet.VehicleRequest;
import com.transportation_management_system.tms01.dto.fleet.VehicleResponse;
import com.transportation_management_system.tms01.dto.fleet.VehicleTypeRequest;
import com.transportation_management_system.tms01.dto.fleet.VehicleTypeResponse;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.fleet.VehicleType;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.repository.fleet.VehicleRepository;
import com.transportation_management_system.tms01.repository.fleet.VehicleTypeRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeRepository;
import com.transportation_management_system.tms01.service.fleet.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public VehicleResponse createVehicle(VehicleRequest request) {
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new IllegalArgumentException("Biển số xe '" + request.getLicensePlate() + "' đã tồn tại trong hệ thống");
        }

        VehicleType type = null;
        if (request.getVehicleTypeId() != null) {
            type = vehicleTypeRepository.findById(request.getVehicleTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại xe với ID: " + request.getVehicleTypeId()));
        }

        Employee driver = null;
        if (request.getEmployeeId() != null) {
            driver = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ tài xế với ID: " + request.getEmployeeId()));
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleCode(request.getVehicleCode())
                .name(request.getName())
                .licensePlate(request.getLicensePlate())
                .payloadCapacity(request.getPayloadCapacity())
                .status(request.getStatus() != null ? request.getStatus() : "AVAILABLE")
                .inspectionExpirationDate(request.getInspectionExpirationDate())
                .insuranceExpirationDate(request.getInsuranceExpirationDate())
                .vehicleType(type)
                .employee(driver)
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        vehicle = vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findByIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ phương tiện với ID: " + id));

        if (request.getVehicleCode() != null) vehicle.setVehicleCode(request.getVehicleCode());
        if (request.getName() != null) vehicle.setName(request.getName());
        if (request.getLicensePlate() != null && !request.getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
            if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
                throw new IllegalArgumentException("Biển số xe '" + request.getLicensePlate() + "' đã tồn tại trong hệ thống");
            }
            vehicle.setLicensePlate(request.getLicensePlate());
        }
        if (request.getPayloadCapacity() != null) vehicle.setPayloadCapacity(request.getPayloadCapacity());
        if (request.getStatus() != null) vehicle.setStatus(request.getStatus());
        if (request.getInspectionExpirationDate() != null) vehicle.setInspectionExpirationDate(request.getInspectionExpirationDate());
        if (request.getInsuranceExpirationDate() != null) vehicle.setInsuranceExpirationDate(request.getInsuranceExpirationDate());


        if (request.getVehicleTypeId() != null) {
            VehicleType type = vehicleTypeRepository.findById(request.getVehicleTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại xe với ID: " + request.getVehicleTypeId()));
            vehicle.setVehicleType(type);
        }

        if (request.getEmployeeId() != null) {
            Employee driver = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ tài xế với ID: " + request.getEmployeeId()));
            vehicle.setEmployee(driver);
        }

        vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ phương tiện với ID: " + id));

        vehicle.setIsDelete(true);
        vehicle.setDeleteDate(LocalDateTime.now());
        vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ phương tiện với ID: " + id));
        return mapToResponse(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehiclesByStatus(String status) {
        return vehicleRepository.findByStatusAndIsDeleteFalse(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehiclesByTypeCode(String typeCode) {
        return vehicleRepository.findByVehicleType_VehicleTypeCodeAndIsDeleteFalse(typeCode).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleTypeResponse> getAllVehicleTypes() {
        return vehicleTypeRepository.findAll().stream()
                .map(this::mapToTypeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehicleTypeResponse createVehicleType(VehicleTypeRequest request) {
        if (vehicleTypeRepository.existsByVehicleTypeCode(request.getVehicleTypeCode())) {
            throw new IllegalArgumentException("Mã loại xe '" + request.getVehicleTypeCode() + "' đã tồn tại");
        }

        VehicleType type = VehicleType.builder()
                .vehicleTypeCode(request.getVehicleTypeCode())
                .vehicleTypeName(request.getVehicleTypeName())
                .description(request.getDescription())
                .build();

        type = vehicleTypeRepository.save(type);
        return mapToTypeResponse(type);
    }

    @Override
    @Transactional
    public VehicleTypeResponse updateVehicleType(Long id, VehicleTypeRequest request) {
        VehicleType type = vehicleTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại xe với ID: " + id));

        if (request.getVehicleTypeName() != null) type.setVehicleTypeName(request.getVehicleTypeName());
        if (request.getDescription() != null) type.setDescription(request.getDescription());

        vehicleTypeRepository.save(type);
        return mapToTypeResponse(type);
    }

    @Override
    @Transactional
    public void deleteVehicleType(Long id) {
        VehicleType type = vehicleTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại xe với ID: " + id));

        vehicleTypeRepository.delete(type);
    }

    private VehicleResponse mapToResponse(Vehicle v) {
        String driverName = null;
        String driverPhone = null;
        if (v.getEmployee() != null) {
            driverName = (v.getEmployee().getFirstname() != null ? v.getEmployee().getFirstname() : "") + " " +
                         (v.getEmployee().getLastname() != null ? v.getEmployee().getLastname() : "");
            driverName = driverName.trim();
            driverPhone = v.getEmployee().getPhone();
        }

        return VehicleResponse.builder()
                .id(v.getId())
                .vehicleCode(v.getVehicleCode())
                .name(v.getName())
                .licensePlate(v.getLicensePlate())
                .payloadCapacity(v.getPayloadCapacity())
                .status(v.getStatus())
                .inspectionExpirationDate(v.getInspectionExpirationDate())
                .insuranceExpirationDate(v.getInsuranceExpirationDate())
                .employeeId(v.getEmployee() != null ? v.getEmployee().getEmployeeId() : null)
                .driverName(driverName)
                .driverPhone(driverPhone)
                .vehicleTypeId(v.getVehicleType() != null ? v.getVehicleType().getVehicleTypeId() : null)
                .vehicleTypeCode(v.getVehicleType() != null ? v.getVehicleType().getVehicleTypeCode() : null)
                .vehicleTypeName(v.getVehicleType() != null ? v.getVehicleType().getVehicleTypeName() : null)
                .createDate(v.getCreateDate())
                .build();
    }

    private VehicleTypeResponse mapToTypeResponse(VehicleType t) {
        return VehicleTypeResponse.builder()
                .vehicleTypeId(t.getVehicleTypeId())
                .vehicleTypeCode(t.getVehicleTypeCode())
                .vehicleTypeName(t.getVehicleTypeName())
                .description(t.getDescription())
                .build();
    }
}
