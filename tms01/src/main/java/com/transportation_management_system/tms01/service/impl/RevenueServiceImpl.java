package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.revenue.*;
import com.transportation_management_system.tms01.entity.expense.Expense;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.payroll.Salary;
import com.transportation_management_system.tms01.entity.revenue.*;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import com.transportation_management_system.tms01.repository.expense.ExpenseRepository;
import com.transportation_management_system.tms01.repository.fleet.VehicleRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeRepository;
import com.transportation_management_system.tms01.repository.payroll.SalaryRepository;
import com.transportation_management_system.tms01.repository.revenue.*;
import com.transportation_management_system.tms01.repository.shipment.ShipmentRepository;
import com.transportation_management_system.tms01.repository.shipment.StatusEnumRepository;
import com.transportation_management_system.tms01.service.revenue.RevenueService;
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
public class RevenueServiceImpl implements RevenueService {

    private final RevenueFinalRepository revenueFinalRepository;
    private final RevenueShipmentRepository revenueShipmentRepository;
    private final ShipmentRepository shipmentRepository;
    private final ExpenseRepository expenseRepository;
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final StatusEnumRepository statusEnumRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional(readOnly = true)
    public RevenueSummaryResponse getRevenueSummary() {
        List<Shipment> allShipments = shipmentRepository.findAllByIsDeleteFalse();
        BigDecimal grossRevenue = allShipments.stream()
                .map(s -> s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Expense> allExpenses = expenseRepository.findAllByIsDeleteFalse();
        BigDecimal totalExpenses = allExpenses.stream()
                .map(e -> e.getTotalExpense() != null ? e.getTotalExpense() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Cộng chi phí phát sinh từ tất cả các chuyến hàng
        BigDecimal totalShipmentIncurredCosts = allShipments.stream()
                .map(s -> s.getIncurredCosts() != null ? s.getIncurredCosts() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalExpenses = totalExpenses.add(totalShipmentIncurredCosts);


        List<Salary> allSalaries = salaryRepository.findAllByIsDeleteFalse();
        BigDecimal totalSalariesPaid = allSalaries.stream()
                .map(s -> s.getSalaryCosts() != null ? s.getSalaryCosts() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netProfit = grossRevenue.subtract(totalExpenses).subtract(totalSalariesPaid);

        int totalShipmentsCompleted = (int) allShipments.stream()
                .filter(s -> s.getStatusEnum() != null && "DELIVERED".equals(s.getStatusEnum().getStatusEnumCode()))
                .count();

        int activeDriversCount = (int) employeeRepository.findAllByIsDeleteFalse().stream()
                .filter(e -> e.getEmployeeType() != null && ("DRIVER".equals(e.getEmployeeType().getEmployeeTypeCode()) ||
                        (e.getEmployeeType().getEmployeeTypeName() != null && e.getEmployeeType().getEmployeeTypeName().contains("L\u00E1i"))))
                .count();

        return RevenueSummaryResponse.builder()
                .totalGrossRevenue(grossRevenue)
                .totalExpenses(totalExpenses)
                .totalSalariesPaid(totalSalariesPaid)
                .netProfit(netProfit)
                .totalShipmentsCompleted(totalShipmentsCompleted)
                .activeDriversCount(activeDriversCount)
                .build();
    }

    @Override
    @Transactional
    public RevenueFinalResponse generateAndCreateRevenueFinal(RevenueFinalRequest request) {
        if (revenueFinalRepository.existsByRevenueCode(request.getRevenueCode())) {
            throw new IllegalArgumentException("M\u00E3 b\u00E1o c\u00E1o doanh thu '" + request.getRevenueCode() + "' \u0111\u00E3 t\u1ED3n t\u1EA1i");
        }

        List<Shipment> linkedShipments = new ArrayList<>();
        if (request.getShipmentIds() != null && !request.getShipmentIds().isEmpty()) {
            linkedShipments = shipmentRepository.findAllById(request.getShipmentIds());
        } else {
            linkedShipments = shipmentRepository.findAllByIsDeleteFalse();
        }

        BigDecimal grossRevenue = linkedShipments.stream()
                .map(s -> s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.getGrossRevenue() != null && request.getGrossRevenue().compareTo(BigDecimal.ZERO) > 0) {
            grossRevenue = request.getGrossRevenue();
        }

        BigDecimal totalExpenses = expenseRepository.findAllByIsDeleteFalse().stream()
                .map(e -> e.getTotalExpense() != null ? e.getTotalExpense() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Cộng thêm chi phí phát sinh từ các chuyến hàng liên quan (Shipment Incurred Costs)
        BigDecimal shipmentIncurredCostsSum = linkedShipments.stream()
                .map(s -> s.getIncurredCosts() != null ? s.getIncurredCosts() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalExpenses = totalExpenses.add(shipmentIncurredCostsSum);

        if (request.getTotalExpense() != null && request.getTotalExpense().compareTo(BigDecimal.ZERO) > 0) {
            totalExpenses = request.getTotalExpense();
        }


        BigDecimal totalSalary = salaryRepository.findAllByIsDeleteFalse().stream()
                .map(s -> s.getSalaryCosts() != null ? s.getSalaryCosts() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.getTotalSalary() != null && request.getTotalSalary().compareTo(BigDecimal.ZERO) > 0) {
            totalSalary = request.getTotalSalary();
        }

        BigDecimal netProfit = grossRevenue.subtract(totalExpenses).subtract(totalSalary);
        if (request.getRevenueFinalCosts() != null && request.getRevenueFinalCosts().compareTo(BigDecimal.ZERO) > 0) {
            netProfit = request.getRevenueFinalCosts();
        }

        StatusEnum status = statusEnumRepository.findByStatusEnumCode("DELIVERED").orElse(null);

        Vehicle vehicle = null;
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findByIdAndIsDeleteFalse(request.getVehicleId()).orElse(null);
        }

        RevenueFinal rf = RevenueFinal.builder()
                .revenueCode(request.getRevenueCode())
                .title(request.getTitle() != null ? request.getTitle() : "B\u00E1o c\u00E1o ch\u1ED1t doanh thu - " + request.getRevenueCode())
                .vehicle(vehicle)
                .licensePlate(vehicle != null ? vehicle.getLicensePlate() : null)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalShipment(linkedShipments.size())
                .grossRevenue(grossRevenue)
                .totalExpense(totalExpenses)
                .totalSalary(totalSalary)
                .revenueFinalCosts(netProfit)
                .notes(request.getNotes())
                .statusEnum(status)
                .statusEnumCode("DELIVERED")
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        rf = revenueFinalRepository.save(rf);

        for (Shipment s : linkedShipments) {
            RevenueShipment rs = RevenueShipment.builder()
                    .revenueFinal(rf)
                    .shipment(s)
                    .build();
            revenueShipmentRepository.save(rs);
        }

        return mapRevenueToResponse(rf);
    }

    @Override
    @Transactional
    public RevenueFinalResponse updateRevenueFinal(Long id, RevenueFinalRequest request) {
        RevenueFinal rf = revenueFinalRepository.findByRevenueIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y b\u00E1o c\u00E1o doanh thu v\u1EDBi ID: " + id));

        if (request.getTitle() != null) rf.setTitle(request.getTitle());
        if (request.getVehicleId() != null) {
            Vehicle v = vehicleRepository.findByIdAndIsDeleteFalse(request.getVehicleId()).orElse(null);
            rf.setVehicle(v);
            rf.setLicensePlate(v != null ? v.getLicensePlate() : null);
        }
        if (request.getStartDate() != null) rf.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) rf.setEndDate(request.getEndDate());
        if (request.getGrossRevenue() != null) rf.setGrossRevenue(request.getGrossRevenue());
        if (request.getTotalExpense() != null) rf.setTotalExpense(request.getTotalExpense());
        if (request.getTotalSalary() != null) rf.setTotalSalary(request.getTotalSalary());
        if (request.getRevenueFinalCosts() != null) rf.setRevenueFinalCosts(request.getRevenueFinalCosts());
        if (request.getNotes() != null) rf.setNotes(request.getNotes());

        final RevenueFinal updatedRf = revenueFinalRepository.save(rf);

        // Nếu có shipmentIds gửi lên khi cập nhật, làm mới các liên kết chuyến hàng
        if (request.getShipmentIds() != null) {
            revenueShipmentRepository.deleteByRevenueFinal_RevenueId(id);
            revenueShipmentRepository.flush();
            if (!request.getShipmentIds().isEmpty()) {

                List<Shipment> shipments = shipmentRepository.findAllById(request.getShipmentIds());
                for (Shipment s : shipments) {
                    RevenueShipment rs = RevenueShipment.builder()
                            .revenueFinal(updatedRf)
                            .shipment(s)
                            .build();
                    revenueShipmentRepository.save(rs);
                }
            }
        }

        return mapRevenueToResponse(updatedRf);
    }


    @Override
    @Transactional(readOnly = true)
    public RevenueFinalResponse getRevenueFinalById(Long id) {
        RevenueFinal rf = revenueFinalRepository.findByRevenueIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y b\u00E1o c\u00E1o doanh thu v\u1EDBi ID: " + id));
        return mapRevenueToResponse(rf);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RevenueFinalResponse> getAllRevenueFinals() {
        return revenueFinalRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapRevenueToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRevenueFinal(Long id) {
        RevenueFinal rf = revenueFinalRepository.findByRevenueIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Kh\u00F4ng t\u00ECm th\u1EA5y b\u00E1o c\u00E1o doanh thu v\u1EDBi ID: " + id));

        rf.setIsDelete(true);
        rf.setDeleteDate(LocalDateTime.now());
        revenueFinalRepository.save(rf);
    }

    private RevenueFinalResponse mapRevenueToResponse(RevenueFinal rf) {
        List<String> shipmentCodes = revenueShipmentRepository.findByRevenueFinal_RevenueId(rf.getRevenueId()).stream()
                .filter(rs -> rs.getShipment() != null)
                .map(rs -> rs.getShipment().getShipmentCode())
                .collect(Collectors.toList());

        return RevenueFinalResponse.builder()
                .revenueId(rf.getRevenueId())
                .revenueCode(rf.getRevenueCode())
                .title(rf.getTitle())
                .vehicleId(rf.getVehicle() != null ? rf.getVehicle().getId() : null)
                .licensePlate(rf.getLicensePlate())
                .vehicleName(rf.getVehicle() != null ? rf.getVehicle().getName() : null)
                .startDate(rf.getStartDate())
                .endDate(rf.getEndDate())
                .totalShipment(rf.getTotalShipment() != null ? rf.getTotalShipment() : 0)
                .grossRevenue(rf.getGrossRevenue() != null ? rf.getGrossRevenue() : BigDecimal.ZERO)
                .totalExpense(rf.getTotalExpense() != null ? rf.getTotalExpense() : BigDecimal.ZERO)
                .totalSalary(rf.getTotalSalary() != null ? rf.getTotalSalary() : BigDecimal.ZERO)
                .revenueFinalCosts(rf.getRevenueFinalCosts() != null ? rf.getRevenueFinalCosts() : BigDecimal.ZERO)
                .notes(rf.getNotes())
                .createDate(rf.getCreateDate())
                .shipmentCodes(shipmentCodes)
                .build();
    }
}
