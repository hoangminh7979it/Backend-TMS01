package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.shipment.ShipmentRequest;
import com.transportation_management_system.tms01.dto.shipment.ShipmentResponse;
import com.transportation_management_system.tms01.dto.shipment.StatusEnumRequest;
import com.transportation_management_system.tms01.dto.shipment.StatusEnumResponse;
import com.transportation_management_system.tms01.entity.customer.Customer;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import com.transportation_management_system.tms01.repository.customer.CustomerRepository;
import com.transportation_management_system.tms01.repository.fleet.VehicleRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeRepository;
import com.transportation_management_system.tms01.repository.shipment.ShipmentRepository;
import com.transportation_management_system.tms01.repository.shipment.StatusEnumRepository;
import com.transportation_management_system.tms01.service.shipment.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final StatusEnumRepository statusEnumRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public ShipmentResponse createShipment(ShipmentRequest request) {
        if (shipmentRepository.existsByShipmentCode(request.getShipmentCode())) {
            throw new IllegalArgumentException("Mã đơn hàng '" + request.getShipmentCode() + "' đã tồn tại trong hệ thống");
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findByCustomerIdAndIsDeleteFalse(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
        }

        Vehicle vehicle = null;
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findByIdAndIsDeleteFalse(request.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phương tiện với ID: " + request.getVehicleId()));
        }

        Employee driver = null;
        if (request.getEmployeeId() != null) {
            driver = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài xế với ID: " + request.getEmployeeId()));
        }

        Employee coDriver = null;
        if (request.getCoDriverId() != null) {
            coDriver = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getCoDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phụ xe với ID: " + request.getCoDriverId()));
        }


        StatusEnum status = null;
        if (request.getStatusEnumId() != null) {
            status = statusEnumRepository.findById(request.getStatusEnumId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trạng thái với ID: " + request.getStatusEnumId()));
        } else {
            status = statusEnumRepository.findByStatusEnumCode("CREATED").orElse(null);
        }

        Shipment shipment = Shipment.builder()
                .shipmentCode(request.getShipmentCode())
                .cargoType(request.getCargoType())
                .receiptPlace(request.getReceiptPlace())
                .deliveryPlace(request.getDeliveryPlace())
                .weight(request.getWeight())
                .dateOfReceipt(request.getDateOfReceipt())
                .deliveryDate(request.getDeliveryDate())
                .revenue(request.getRevenue() != null ? request.getRevenue() : BigDecimal.ZERO)
                .incurredCosts(request.getIncurredCosts() != null ? request.getIncurredCosts() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .customer(customer)
                .vehicle(vehicle)
                .employee(driver)
                .coDriver(coDriver)
                .statusEnum(status)
                .isDelete(false)

                .createDate(LocalDateTime.now())
                .build();

        shipment = shipmentRepository.save(shipment);
        return mapToResponse(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponse updateShipment(Long id, ShipmentRequest request) {
        Shipment shipment = shipmentRepository.findByShipmentIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));

        if (request.getCargoType() != null) shipment.setCargoType(request.getCargoType());
        if (request.getReceiptPlace() != null) shipment.setReceiptPlace(request.getReceiptPlace());
        if (request.getDeliveryPlace() != null) shipment.setDeliveryPlace(request.getDeliveryPlace());
        if (request.getWeight() != null) shipment.setWeight(request.getWeight());
        if (request.getDateOfReceipt() != null) shipment.setDateOfReceipt(request.getDateOfReceipt());
        if (request.getDeliveryDate() != null) shipment.setDeliveryDate(request.getDeliveryDate());
        if (request.getRevenue() != null) shipment.setRevenue(request.getRevenue());
        if (request.getIncurredCosts() != null) shipment.setIncurredCosts(request.getIncurredCosts());
        if (request.getNotes() != null) shipment.setNotes(request.getNotes());

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerIdAndIsDeleteFalse(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
            shipment.setCustomer(customer);
        }

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findByIdAndIsDeleteFalse(request.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phương tiện với ID: " + request.getVehicleId()));
            shipment.setVehicle(vehicle);
        }

        if (request.getEmployeeId() != null) {
            Employee driver = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài xế với ID: " + request.getEmployeeId()));
            shipment.setEmployee(driver);
        } else if (request.getEmployeeId() == null && request.getVehicleId() != null) {
            // allow unsetting driver if explicitly sent as null
            shipment.setEmployee(null);
        }

        if (request.getCoDriverId() != null) {
            Employee coDriverEntity = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getCoDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phụ xe với ID: " + request.getCoDriverId()));
            shipment.setCoDriver(coDriverEntity);
        } else {
            shipment.setCoDriver(null);
        }


        if (request.getStatusEnumId() != null) {
            StatusEnum status = statusEnumRepository.findById(request.getStatusEnumId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trạng thái với ID: " + request.getStatusEnumId()));
            shipment.setStatusEnum(status);
        }

        shipmentRepository.save(shipment);
        return mapToResponse(shipment);
    }

    @Override
    @Transactional
    public void deleteShipment(Long id) {
        Shipment shipment = shipmentRepository.findByShipmentIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));

        shipment.setIsDelete(true);
        shipment.setDeleteDate(LocalDateTime.now());
        shipmentRepository.save(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findByShipmentIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));
        return mapToResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponse> getShipmentsByStatusCode(String statusCode) {
        return shipmentRepository.findByStatusEnum_StatusEnumCodeAndIsDeleteFalse(statusCode).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShipmentResponse updateShipmentStatus(Long id, String statusCode) {
        Shipment shipment = shipmentRepository.findByShipmentIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));

        StatusEnum status = statusEnumRepository.findByStatusEnumCode(statusCode)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trạng thái đơn hàng với mã: " + statusCode));

        shipment.setStatusEnum(status);
        shipmentRepository.save(shipment);
        return mapToResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusEnumResponse> getAllStatuses() {
        return statusEnumRepository.findAll().stream()
                .map(this::mapToStatusResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StatusEnumResponse createStatus(StatusEnumRequest request) {
        if (statusEnumRepository.existsByStatusEnumCode(request.getStatusEnumCode())) {
            throw new IllegalArgumentException("Mã trạng thái '" + request.getStatusEnumCode() + "' đã tồn tại");
        }

        StatusEnum status = StatusEnum.builder()
                .statusEnumCode(request.getStatusEnumCode())
                .statusEnumName(request.getStatusEnumName())
                .description(request.getDescription())
                .build();

        status = statusEnumRepository.save(status);
        return mapToStatusResponse(status);
    }

    @Override
    @Transactional
    public StatusEnumResponse updateStatus(Long id, StatusEnumRequest request) {
        StatusEnum status = statusEnumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trạng thái với ID: " + id));

        if (request.getStatusEnumName() != null) status.setStatusEnumName(request.getStatusEnumName());
        if (request.getDescription() != null) status.setDescription(request.getDescription());

        statusEnumRepository.save(status);
        return mapToStatusResponse(status);
    }

    @Override
    @Transactional
    public void deleteStatus(Long id) {
        StatusEnum status = statusEnumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trạng thái với ID: " + id));

        statusEnumRepository.delete(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponse> getShipmentsByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end   = endDate   != null ? endDate.atTime(23, 59, 59) : null;

        List<Shipment> result;
        if (start != null && end != null) {
            result = shipmentRepository.findByEmployeeAndBothDates(employeeId, start, end);
        } else if (start != null) {
            result = shipmentRepository.findByEmployeeAndStartDate(employeeId, start);
        } else if (end != null) {
            result = shipmentRepository.findByEmployeeAndEndDate(employeeId, end);
        } else {
            result = shipmentRepository.findByEmployeeId(employeeId);
        }

        return result.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ShipmentResponse mapToResponse(Shipment s) {
        BigDecimal revenue = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
        BigDecimal costs = s.getIncurredCosts() != null ? s.getIncurredCosts() : BigDecimal.ZERO;
        BigDecimal netProfit = revenue.subtract(costs);

        String customerName = null;
        String customerPhone = null;
        if (s.getCustomer() != null) {
            customerName = s.getCustomer().getCompanyName() != null ? s.getCustomer().getCompanyName() :
                    (s.getCustomer().getFirstname() + " " + (s.getCustomer().getLastname() != null ? s.getCustomer().getLastname() : "")).trim();
            customerPhone = s.getCustomer().getPhone();
        }

        String driverName = null;
        String driverPhone = null;
        if (s.getEmployee() != null) {
            driverName = (s.getEmployee().getFirstname() != null ? s.getEmployee().getFirstname() : "") + " " +
                         (s.getEmployee().getLastname() != null ? s.getEmployee().getLastname() : "");
            driverName = driverName.trim();
            driverPhone = s.getEmployee().getPhone();
        }

        String coDriverName = null;
        String coDriverPhone = null;
        if (s.getCoDriver() != null) {
            coDriverName = (s.getCoDriver().getFirstname() != null ? s.getCoDriver().getFirstname() : "") + " " +
                           (s.getCoDriver().getLastname() != null ? s.getCoDriver().getLastname() : "");
            coDriverName = coDriverName.trim();
            coDriverPhone = s.getCoDriver().getPhone();
        }


        return ShipmentResponse.builder()
                .shipmentId(s.getShipmentId())
                .shipmentCode(s.getShipmentCode())
                .cargoType(s.getCargoType())
                .receiptPlace(s.getReceiptPlace())
                .deliveryPlace(s.getDeliveryPlace())
                .weight(s.getWeight())
                .dateOfReceipt(s.getDateOfReceipt())
                .deliveryDate(s.getDeliveryDate())
                .revenue(revenue)
                .incurredCosts(costs)
                .netProfit(netProfit)
                .notes(s.getNotes())
                .customerId(s.getCustomer() != null ? s.getCustomer().getCustomerId() : null)
                .customerName(customerName)
                .customerPhone(customerPhone)
                .vehicleId(s.getVehicle() != null ? s.getVehicle().getId() : null)
                .licensePlate(s.getVehicle() != null ? s.getVehicle().getLicensePlate() : null)
                .vehicleName(s.getVehicle() != null ? s.getVehicle().getName() : null)
                .employeeId(s.getEmployee() != null ? s.getEmployee().getEmployeeId() : null)
                .driverName(driverName)
                .driverPhone(driverPhone)
                .coDriverId(s.getCoDriver() != null ? s.getCoDriver().getEmployeeId() : null)
                .coDriverName(coDriverName)
                .coDriverPhone(coDriverPhone)
                .statusEnumId(s.getStatusEnum() != null ? s.getStatusEnum().getStatusEnumId() : null)

                .statusEnumCode(s.getStatusEnum() != null ? s.getStatusEnum().getStatusEnumCode() : null)
                .statusEnumName(s.getStatusEnum() != null ? s.getStatusEnum().getStatusEnumName() : null)
                .createDate(s.getCreateDate())
                .build();
    }

    private StatusEnumResponse mapToStatusResponse(StatusEnum st) {
        return StatusEnumResponse.builder()
                .statusEnumId(st.getStatusEnumId())
                .statusEnumCode(st.getStatusEnumCode())
                .statusEnumName(st.getStatusEnumName())
                .description(st.getDescription())
                .build();
    }
}
