package com.transportation_management_system.tms01.controller.report;

import com.transportation_management_system.tms01.service.report.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ExcelExportService excelExportService;

    @GetMapping("/shipments/export")
    public ResponseEntity<byte[]> exportShipments(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        byte[] data = excelExportService.exportShipments(vehicleId, startDate, endDate);
        return buildFileResponse(data, "Bao_Cao_Don_Hang_Shipments.xlsx");
    }

    @GetMapping("/expenses/export")
    public ResponseEntity<byte[]> exportExpenses(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        byte[] data = excelExportService.exportExpenses(vehicleId, startDate, endDate);
        return buildFileResponse(data, "Bao_Cao_Chi_Phi_Expenses.xlsx");
    }

    @GetMapping("/salaries/export")
    public ResponseEntity<byte[]> exportSalaries(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        byte[] data = excelExportService.exportSalaries(employeeId, startDate, endDate);
        return buildFileResponse(data, "Bao_Cao_Bang_Luong_Salaries.xlsx");
    }

    @GetMapping("/salaries/{salaryId}/export")
    public ResponseEntity<byte[]> exportSalaryById(@PathVariable Long salaryId) {
        byte[] data = excelExportService.exportSalaryById(salaryId);
        return buildFileResponse(data, "Phieu_Luong_" + salaryId + ".xlsx");
    }

    @GetMapping("/revenues/export")
    public ResponseEntity<byte[]> exportRevenues(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        byte[] data = excelExportService.exportRevenues(vehicleId, startDate, endDate);
        return buildFileResponse(data, "Bao_Cao_Doanh_Thu_Revenues.xlsx");
    }

    private ResponseEntity<byte[]> buildFileResponse(byte[] data, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }
}
