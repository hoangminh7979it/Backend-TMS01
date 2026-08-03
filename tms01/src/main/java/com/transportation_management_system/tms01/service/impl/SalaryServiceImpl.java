package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.payroll.*;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.entity.payroll.*;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.repository.fleet.VehicleRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeRepository;
import com.transportation_management_system.tms01.repository.payroll.*;
import com.transportation_management_system.tms01.repository.shipment.ShipmentRepository;
import com.transportation_management_system.tms01.service.payroll.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final SalaryShipmentRepository salaryShipmentRepository;
    private final SalaryVehicleRepository salaryVehicleRepository;
    private final EmployeeRepository employeeRepository;
    private final ShipmentRepository shipmentRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public SalaryResponse calculateAndCreateSalary(SalaryRequest request) {
        if (salaryRepository.existsBySalaryCode(request.getSalaryCode())) {
            throw new IllegalArgumentException("M\u00E3 b\u1EA3ng l\u01B0\u01A1ng '" + request.getSalaryCode() + "' \u0111\u00E3 t\u1ED3n t\u1EA1i");
        }

        Employee employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y nh\u00E2n vi\u00EAn/t\u00E0i x\u1EBF v\u1EDBi ID: " + request.getEmployeeId()));

        // 1. Calculate Base Salary
        BigDecimal perDayRate = request.getSalaryBasicPerDay() != null ? request.getSalaryBasicPerDay() : BigDecimal.ZERO;
        int workDays = request.getWorkDaysCount() != null ? request.getWorkDaysCount() : 0;
        BigDecimal basicCost = request.getSalaryBasicCosts() != null ? request.getSalaryBasicCosts() : BigDecimal.ZERO;

        if (perDayRate.compareTo(BigDecimal.ZERO) > 0 && workDays > 0 && basicCost.compareTo(BigDecimal.ZERO) == 0) {
            basicCost = perDayRate.multiply(BigDecimal.valueOf(workDays));
        }

        // 2. Calculate Trip Salary (percentage or total amount)
        BigDecimal tripPercentage = request.getTripSalaryPercentage() != null ? request.getTripSalaryPercentage() : BigDecimal.ZERO;
        int shipmentCount = request.getTotalShipmentCount() != null ? request.getTotalShipmentCount() : 0;
        
        List<Shipment> linkedShipments = new ArrayList<>();
        if (request.getShipmentIds() != null && !request.getShipmentIds().isEmpty()) {
            linkedShipments = shipmentRepository.findAllById(request.getShipmentIds());
            shipmentCount = linkedShipments.size();
        }

        BigDecimal driverRevenue = request.getDriverShipmentRevenue() != null ? request.getDriverShipmentRevenue() : BigDecimal.ZERO;
        if (driverRevenue.compareTo(BigDecimal.ZERO) == 0 && !linkedShipments.isEmpty()) {
            driverRevenue = linkedShipments.stream()
                    .map(s -> s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        BigDecimal totalPerShipmentSalary = BigDecimal.ZERO;
        if (tripPercentage.compareTo(BigDecimal.ZERO) > 0 && driverRevenue.compareTo(BigDecimal.ZERO) > 0) {
            totalPerShipmentSalary = driverRevenue.multiply(tripPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        if (request.getTotalSalaryPerShipment() != null && request.getTotalSalaryPerShipment().compareTo(BigDecimal.ZERO) > 0) {
            totalPerShipmentSalary = request.getTotalSalaryPerShipment();
        }

        BigDecimal allowances = request.getAllowanceCosts() != null ? request.getAllowanceCosts() : BigDecimal.ZERO;
        BigDecimal deductions = request.getDeductionCosts() != null ? request.getDeductionCosts() : BigDecimal.ZERO;

        BigDecimal totalSalaryCosts = basicCost.add(totalPerShipmentSalary).add(allowances).subtract(deductions);
        if (request.getSalaryCosts() != null && request.getSalaryCosts().compareTo(BigDecimal.ZERO) > 0) {
            totalSalaryCosts = request.getSalaryCosts();
        }

        // Xóa block Resolve vehicle cũ - giờ dùng SalaryVehicle join table
        Salary salary = Salary.builder()
                .salaryCode(request.getSalaryCode())
                .employee(employee)
                .employeeCode(employee.getEmployeeCode())
                .salaryBasicCosts(basicCost)
                .workDaysCount(workDays)
                .salaryBasicPerDay(perDayRate)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalShipmentCount(shipmentCount)
                .driverShipmentRevenue(driverRevenue)
                .tripSalaryPercentage(tripPercentage)
                .totalSalaryPerShipment(totalPerShipmentSalary)
                .allowanceCosts(allowances)
                .deductionCosts(deductions)
                .salaryCosts(totalSalaryCosts)
                .notes(request.getNotes())
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        final Salary savedSalary = salaryRepository.save(salary);

        // Lưu các chuyến hàng liên kết
        if (!linkedShipments.isEmpty()) {
            for (Shipment s : linkedShipments) {
                SalaryShipment ss = SalaryShipment.builder()
                        .salaryMain(savedSalary)
                        .shipment(s)
                        .build();
                salaryShipmentRepository.save(ss);
            }
        }

        // Lưu tất cả phương tiện từ danh sách vehicleIds (do Frontend gửi lên sau khi tổng hợp từ shipments)
        if (request.getVehicleIds() != null && !request.getVehicleIds().isEmpty()) {
            // De-duplicate: mỗi vehicle chỉ lưu 1 lần
            request.getVehicleIds().stream().distinct().forEach(vId -> {
                vehicleRepository.findByIdAndIsDeleteFalse(vId).ifPresent(v -> {
                    SalaryVehicle sv = SalaryVehicle.builder()
                            .salaryMain(savedSalary)
                            .vehicle(v)
                            .build();
                    salaryVehicleRepository.save(sv);
                });
            });
        }

        return mapSalaryToResponse(savedSalary);
    }

    @Override
    @Transactional
    public SalaryResponse updateSalary(Long id, SalaryRequest request) {
        Salary salary = salaryRepository.findBySalaryIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y b\u1EA3ng l\u01B0\u01A1ng v\u1EDBi ID: " + id));

        if (request.getSalaryBasicCosts() != null) salary.setSalaryBasicCosts(request.getSalaryBasicCosts());
        if (request.getWorkDaysCount() != null) salary.setWorkDaysCount(request.getWorkDaysCount());
        if (request.getSalaryBasicPerDay() != null) salary.setSalaryBasicPerDay(request.getSalaryBasicPerDay());
        if (request.getStartDate() != null) salary.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) salary.setEndDate(request.getEndDate());
        if (request.getTotalShipmentCount() != null) salary.setTotalShipmentCount(request.getTotalShipmentCount());
        if (request.getDriverShipmentRevenue() != null) salary.setDriverShipmentRevenue(request.getDriverShipmentRevenue());
        if (request.getTripSalaryPercentage() != null) salary.setTripSalaryPercentage(request.getTripSalaryPercentage());
        if (request.getTotalSalaryPerShipment() != null) salary.setTotalSalaryPerShipment(request.getTotalSalaryPerShipment());
        if (request.getAllowanceCosts() != null) salary.setAllowanceCosts(request.getAllowanceCosts());
        if (request.getDeductionCosts() != null) salary.setDeductionCosts(request.getDeductionCosts());
        if (request.getSalaryCosts() != null) salary.setSalaryCosts(request.getSalaryCosts());
        if (request.getNotes() != null) salary.setNotes(request.getNotes());

        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(request.getEmployeeId()).orElse(null);
            salary.setEmployee(employee);
            if (employee != null) salary.setEmployeeCode(employee.getEmployeeCode());
        }

        final Salary updatedSalary = salaryRepository.save(salary);

        // Cập nhật danh sách phương tiện: xóa cũ, lưu mới
        if (request.getVehicleIds() != null) {
            salaryVehicleRepository.deleteBySalaryMain_SalaryId(id);
            request.getVehicleIds().stream().distinct().forEach(vId -> {
                vehicleRepository.findByIdAndIsDeleteFalse(vId).ifPresent(v -> {
                    SalaryVehicle sv = SalaryVehicle.builder()
                            .salaryMain(updatedSalary)
                            .vehicle(v)
                            .build();
                    salaryVehicleRepository.save(sv);
                });
            });
        }

        return mapSalaryToResponse(updatedSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryResponse getSalaryById(Long id) {
        Salary salary = salaryRepository.findBySalaryIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y b\u1EA3ng l\u01B0\u01A1ng v\u1EDBi ID: " + id));
        return mapSalaryToResponse(salary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryResponse> getAllSalaries() {
        return salaryRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapSalaryToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSalary(Long id) {
        Salary salary = salaryRepository.findBySalaryIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y b\u1EA3ng l\u01B0\u01A1ng v\u1EDBi ID: " + id));

        salary.setIsDelete(true);
        salary.setDeleteDate(LocalDateTime.now());
        salaryRepository.save(salary);
    }

    private SalaryResponse mapSalaryToResponse(Salary s) {
        String empName = null;
        String empTypeCode = null;
        String empTypeName = null;
        String licenseId = null;

        if (s.getEmployee() != null) {
            empName = (s.getEmployee().getFirstname() != null ? s.getEmployee().getFirstname() : "") + " " +
                      (s.getEmployee().getLastname() != null ? s.getEmployee().getLastname() : "");
            empName = empName.trim();
            licenseId = s.getEmployee().getDrivingLicenseId();
            if (s.getEmployee().getEmployeeType() != null) {
                empTypeCode = s.getEmployee().getEmployeeType().getEmployeeTypeCode();
                empTypeName = s.getEmployee().getEmployeeType().getEmployeeTypeName();
            }
        }

        List<String> shipmentCodes = salaryShipmentRepository.findBySalaryMain_SalaryId(s.getSalaryId()).stream()
                .filter(ss -> ss.getShipment() != null)
                .map(ss -> ss.getShipment().getShipmentCode())
                .collect(Collectors.toList());

        // Lấy tất cả biển số phương tiện
        List<String> licensePlates = salaryVehicleRepository.findBySalaryMain_SalaryId(s.getSalaryId()).stream()
                .filter(sv -> sv.getVehicle() != null)
                .map(sv -> sv.getVehicle().getLicensePlate())
                .collect(Collectors.toList());

        return SalaryResponse.builder()
                .salaryId(s.getSalaryId())
                .salaryCode(s.getSalaryCode())
                .employeeId(s.getEmployee() != null ? s.getEmployee().getEmployeeId() : null)
                .employeeCode(s.getEmployeeCode())
                .employeeName(empName)
                .employeeTypeName(empTypeName != null ? empTypeName : empTypeCode)
                .drivingLicenseId(licenseId)
                .licensePlates(licensePlates)
                .salaryBasicCosts(s.getSalaryBasicCosts() != null ? s.getSalaryBasicCosts() : BigDecimal.ZERO)
                .workDaysCount(s.getWorkDaysCount() != null ? s.getWorkDaysCount() : 0)
                .salaryBasicPerDay(s.getSalaryBasicPerDay() != null ? s.getSalaryBasicPerDay() : BigDecimal.ZERO)
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .totalShipmentCount(s.getTotalShipmentCount() != null ? s.getTotalShipmentCount() : 0)
                .driverShipmentRevenue(s.getDriverShipmentRevenue() != null ? s.getDriverShipmentRevenue() : BigDecimal.ZERO)
                .tripSalaryPercentage(s.getTripSalaryPercentage() != null ? s.getTripSalaryPercentage() : BigDecimal.ZERO)
                .totalSalaryPerShipment(s.getTotalSalaryPerShipment() != null ? s.getTotalSalaryPerShipment() : BigDecimal.ZERO)
                .allowanceCosts(s.getAllowanceCosts() != null ? s.getAllowanceCosts() : BigDecimal.ZERO)
                .deductionCosts(s.getDeductionCosts() != null ? s.getDeductionCosts() : BigDecimal.ZERO)
                .salaryCosts(s.getSalaryCosts() != null ? s.getSalaryCosts() : BigDecimal.ZERO)
                .notes(s.getNotes())
                .createDate(s.getCreateDate())
                .shipmentCodes(shipmentCodes)
                .build();
    }
}
