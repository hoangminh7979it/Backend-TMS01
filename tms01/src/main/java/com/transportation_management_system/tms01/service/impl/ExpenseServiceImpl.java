package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.expense.*;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.entity.expense.Expense;
import com.transportation_management_system.tms01.entity.expense.ExpenseDetail;
import com.transportation_management_system.tms01.entity.expense.ExpenseType;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import com.transportation_management_system.tms01.repository.fleet.VehicleRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeRepository;
import com.transportation_management_system.tms01.repository.expense.ExpenseDetailRepository;
import com.transportation_management_system.tms01.repository.expense.ExpenseRepository;
import com.transportation_management_system.tms01.repository.expense.ExpenseTypeRepository;
import com.transportation_management_system.tms01.repository.shipment.ShipmentRepository;
import com.transportation_management_system.tms01.repository.shipment.StatusEnumRepository;
import com.transportation_management_system.tms01.service.expense.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final VehicleRepository vehicleRepository;
    private final ShipmentRepository shipmentRepository;
    private final EmployeeRepository employeeRepository;
    private final StatusEnumRepository statusEnumRepository;

    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        if (expenseRepository.existsByExpenseCode(request.getExpenseCode())) {
            throw new IllegalArgumentException("Mã phiếu chi phí '" + request.getExpenseCode() + "' đã tồn tại trong hệ thống");
        }

        Vehicle vehicle = null;
        String licensePlate = request.getVehicleLicensePlate();
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findByIdAndIsDeleteFalse(request.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phương tiện với ID: " + request.getVehicleId()));
            licensePlate = vehicle.getLicensePlate();
        }

        Shipment shipment = null;
        if (request.getShipmentId() != null) {
            shipment = shipmentRepository.findByShipmentIdAndIsDeleteFalse(request.getShipmentId())
                    .orElse(null);
        }

        Employee employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                    .orElse(null);
        }

        StatusEnum status = null;
        if (request.getStatusEnumId() != null) {
            status = statusEnumRepository.findById(request.getStatusEnumId()).orElse(null);
        }

        Expense expense = Expense.builder()
                .expenseCode(request.getExpenseCode())
                .title(request.getTitle() != null ? request.getTitle() : "Phiếu Chi Phí - " + request.getExpenseCode())
                .expenseDate(request.getExpenseDate() != null ? request.getExpenseDate() : LocalDateTime.now())
                .totalExpense(request.getTotalExpense() != null ? request.getTotalExpense() : BigDecimal.ZERO)
                .vehicleLicensePlate(licensePlate)
                .vehicle(vehicle)
                .shipment(shipment)
                .employee(employee)
                .statusEnum(status)
                .notes(request.getNotes())
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        expense = expenseRepository.save(expense);

        // Save details if provided
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        List<ExpenseDetailResponse> detailResponses = new ArrayList<>();

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            int detailIdx = 1;
            for (ExpenseDetailRequest dtReq : request.getDetails()) {
                ExpenseType et = null;
                if (dtReq.getExpenseTypeId() != null) {
                    et = expenseTypeRepository.findById(dtReq.getExpenseTypeId()).orElse(null);
                }

                BigDecimal detailCost = dtReq.getExpenseDetailCosts() != null ? dtReq.getExpenseDetailCosts() : BigDecimal.ZERO;
                calculatedTotal = calculatedTotal.add(detailCost);

                String detailCode = dtReq.getExpenseDetailCode() != null ? dtReq.getExpenseDetailCode() :
                        expense.getExpenseCode() + "-DT" + String.format("%02d", detailIdx++);

                ExpenseDetail detail = ExpenseDetail.builder()
                        .expenseDetailCode(detailCode)
                        .expense(expense)
                        .expenseType(et)
                        .expenseTypeCode(et != null ? et.getExpenseTypeCode() : dtReq.getExpenseTypeCode())
                        .expenseDetailCosts(detailCost)
                        .description(dtReq.getDescription())
                        .date(dtReq.getDate())
                        .build();

                detail = expenseDetailRepository.save(detail);
                detailResponses.add(mapDetailToResponse(detail));
            }

            if (request.getTotalExpense() == null || request.getTotalExpense().compareTo(BigDecimal.ZERO) == 0) {
                expense.setTotalExpense(calculatedTotal);
                expenseRepository.save(expense);
            }
        }

        ExpenseResponse response = mapToResponse(expense);
        response.setDetails(detailResponses);
        return response;
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findByExpenseIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu chi phí với ID: " + id));

        if (request.getTitle() != null) expense.setTitle(request.getTitle());
        if (request.getExpenseDate() != null) expense.setExpenseDate(request.getExpenseDate());
        if (request.getTotalExpense() != null) expense.setTotalExpense(request.getTotalExpense());
        if (request.getNotes() != null) expense.setNotes(request.getNotes());

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findByIdAndIsDeleteFalse(request.getVehicleId())
                    .orElse(null);
            expense.setVehicle(vehicle);
            if (vehicle != null) {
                expense.setVehicleLicensePlate(vehicle.getLicensePlate());
            }
        }

        if (request.getShipmentId() != null) {
            Shipment shipment = shipmentRepository.findByShipmentIdAndIsDeleteFalse(request.getShipmentId())
                    .orElse(null);
            expense.setShipment(shipment);
        }

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                    .orElse(null);
            expense.setEmployee(employee);
        }

        if (request.getStatusEnumId() != null) {
            StatusEnum status = statusEnumRepository.findById(request.getStatusEnumId()).orElse(null);
            expense.setStatusEnum(status);
        }

        expenseRepository.save(expense);

        // Update details if provided
        if (request.getDetails() != null) {
            expenseDetailRepository.deleteByExpense_ExpenseId(expense.getExpenseId());
            expenseDetailRepository.flush();
            int detailIdx = 1;
            BigDecimal calculatedTotal = BigDecimal.ZERO;
            for (ExpenseDetailRequest dtReq : request.getDetails()) {

                ExpenseType et = null;
                if (dtReq.getExpenseTypeId() != null) {
                    et = expenseTypeRepository.findById(dtReq.getExpenseTypeId()).orElse(null);
                }

                BigDecimal detailCost = dtReq.getExpenseDetailCosts() != null ? dtReq.getExpenseDetailCosts() : BigDecimal.ZERO;
                calculatedTotal = calculatedTotal.add(detailCost);

                String detailCode = dtReq.getExpenseDetailCode() != null ? dtReq.getExpenseDetailCode() :
                        expense.getExpenseCode() + "-DT" + String.format("%02d", detailIdx++);

                ExpenseDetail detail = ExpenseDetail.builder()
                        .expenseDetailCode(detailCode)
                        .expense(expense)
                        .expenseType(et)
                        .expenseTypeCode(et != null ? et.getExpenseTypeCode() : dtReq.getExpenseTypeCode())
                        .expenseDetailCosts(detailCost)
                        .description(dtReq.getDescription())
                        .date(dtReq.getDate())
                        .build();

                expenseDetailRepository.save(detail);
            }
        }

        return getExpenseById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findByExpenseIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu chi phí với ID: " + id));

        ExpenseResponse response = mapToResponse(expense);
        List<ExpenseDetailResponse> details = expenseDetailRepository.findByExpense_ExpenseId(id).stream()
                .map(this::mapDetailToResponse)
                .collect(Collectors.toList());
        response.setDetails(details);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAllByIsDeleteFalse().stream()
                .map(e -> {
                    ExpenseResponse response = mapToResponse(e);
                    List<ExpenseDetailResponse> details = expenseDetailRepository.findByExpense_ExpenseId(e.getExpenseId()).stream()
                            .map(this::mapDetailToResponse)
                            .collect(Collectors.toList());
                    response.setDetails(details);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findByExpenseIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu chi phí với ID: " + id));

        expense.setIsDelete(true);
        expense.setDeleteDate(LocalDateTime.now());
        expenseRepository.save(expense);
    }

    // --- EXPENSE TYPES ---

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseTypeResponse> getAllExpenseTypes() {
        return expenseTypeRepository.findAll().stream()
                .map(this::mapTypeToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExpenseTypeResponse createExpenseType(ExpenseTypeRequest request) {
        if (expenseTypeRepository.existsByExpenseTypeCode(request.getExpenseTypeCode())) {
            throw new IllegalArgumentException("Mã loại chi phí '" + request.getExpenseTypeCode() + "' đã tồn tại");
        }

        ExpenseType type = ExpenseType.builder()
                .expenseTypeCode(request.getExpenseTypeCode())
                .expenseTypeName(request.getExpenseTypeName())
                .description(request.getDescription())
                .build();

        type = expenseTypeRepository.save(type);
        return mapTypeToResponse(type);
    }

    @Override
    @Transactional
    public ExpenseTypeResponse updateExpenseType(Long id, ExpenseTypeRequest request) {
        ExpenseType type = expenseTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại chi phí với ID: " + id));

        if (request.getExpenseTypeName() != null) type.setExpenseTypeName(request.getExpenseTypeName());
        if (request.getDescription() != null) type.setDescription(request.getDescription());

        type = expenseTypeRepository.save(type);
        return mapTypeToResponse(type);
    }

    @Override
    @Transactional
    public void deleteExpenseType(Long id) {
        ExpenseType type = expenseTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại chi phí với ID: " + id));

        expenseTypeRepository.delete(type);
    }

    // --- MAPPING HELPERS ---

    private ExpenseResponse mapToResponse(Expense e) {
        String empName = null;
        if (e.getEmployee() != null) {
            empName = (e.getEmployee().getFirstname() != null ? e.getEmployee().getFirstname() : "") + " " +
                      (e.getEmployee().getLastname() != null ? e.getEmployee().getLastname() : "");
            empName = empName.trim();
        }

        return ExpenseResponse.builder()
                .expenseId(e.getExpenseId())
                .expenseCode(e.getExpenseCode())
                .title(e.getTitle())
                .expenseDate(e.getExpenseDate())
                .totalExpense(e.getTotalExpense() != null ? e.getTotalExpense() : BigDecimal.ZERO)
                .vehicleLicensePlate(e.getVehicleLicensePlate() != null ? e.getVehicleLicensePlate() :
                        (e.getVehicle() != null ? e.getVehicle().getLicensePlate() : null))
                .vehicleId(e.getVehicle() != null ? e.getVehicle().getId() : null)
                .vehicleName(e.getVehicle() != null ? e.getVehicle().getName() : null)
                .shipmentId(e.getShipment() != null ? e.getShipment().getShipmentId() : null)
                .shipmentCode(e.getShipment() != null ? e.getShipment().getShipmentCode() : null)
                .employeeId(e.getEmployee() != null ? e.getEmployee().getEmployeeId() : null)
                .employeeName(empName)
                .statusEnumId(e.getStatusEnum() != null ? e.getStatusEnum().getStatusEnumId() : null)
                .statusEnumCode(e.getStatusEnum() != null ? e.getStatusEnum().getStatusEnumCode() : null)
                .statusEnumName(e.getStatusEnum() != null ? e.getStatusEnum().getStatusEnumName() : null)
                .notes(e.getNotes())
                .createDate(e.getCreateDate())
                .build();
    }

    private ExpenseDetailResponse mapDetailToResponse(ExpenseDetail d) {
        return ExpenseDetailResponse.builder()
                .expenseDetailId(d.getExpenseDetailId())
                .expenseDetailCode(d.getExpenseDetailCode())
                .expenseTypeId(d.getExpenseType() != null ? d.getExpenseType().getExpenseTypeId() : null)
                .expenseTypeCode(d.getExpenseType() != null ? d.getExpenseType().getExpenseTypeCode() : d.getExpenseTypeCode())
                .expenseTypeName(d.getExpenseType() != null ? d.getExpenseType().getExpenseTypeName() : null)
                .expenseDetailCosts(d.getExpenseDetailCosts() != null ? d.getExpenseDetailCosts() : BigDecimal.ZERO)
                .description(d.getDescription())
                .date(d.getDate())
                .build();
    }

    private ExpenseTypeResponse mapTypeToResponse(ExpenseType t) {
        return ExpenseTypeResponse.builder()
                .expenseTypeId(t.getExpenseTypeId())
                .expenseTypeCode(t.getExpenseTypeCode())
                .expenseTypeName(t.getExpenseTypeName())
                .description(t.getDescription())
                .build();
    }
}
