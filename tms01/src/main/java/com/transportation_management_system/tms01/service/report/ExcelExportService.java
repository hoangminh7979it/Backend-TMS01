package com.transportation_management_system.tms01.service.report;

public interface ExcelExportService {

    byte[] exportShipments(Long vehicleId, String startDate, String endDate);

    byte[] exportExpenses(Long vehicleId, String startDate, String endDate);

    byte[] exportSalaries(Long employeeId, String startDate, String endDate);

    byte[] exportSalaryById(Long salaryId);
    byte[] exportSalaryById(Long salaryId, org.springframework.web.multipart.MultipartFile templateFile);

    byte[] exportRevenues(Long vehicleId, String startDate, String endDate);

    byte[] exportRevenueById(Long revenueId);
    byte[] exportRevenueById(Long revenueId, org.springframework.web.multipart.MultipartFile templateFile);
}



