package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.entity.expense.Expense;
import com.transportation_management_system.tms01.entity.payroll.Salary;
import com.transportation_management_system.tms01.entity.payroll.SalaryShipment;
import com.transportation_management_system.tms01.entity.revenue.RevenueFinal;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.repository.expense.ExpenseRepository;
import com.transportation_management_system.tms01.repository.payroll.SalaryRepository;
import com.transportation_management_system.tms01.repository.payroll.SalaryShipmentRepository;
import com.transportation_management_system.tms01.repository.revenue.RevenueFinalRepository;
import com.transportation_management_system.tms01.repository.shipment.ShipmentRepository;
import com.transportation_management_system.tms01.service.report.ExcelExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportServiceImpl implements ExcelExportService {

    private final ShipmentRepository shipmentRepository;
    private final ExpenseRepository expenseRepository;
    private final SalaryRepository salaryRepository;
    private final SalaryShipmentRepository salaryShipmentRepository;
    private final RevenueFinalRepository revenueFinalRepository;


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional(readOnly = true)
    public byte[] exportShipments(Long vehicleId, String startDateStr, String endDateStr) {
        List<Shipment> shipments = shipmentRepository.findAllByIsDeleteFalse();

        if (vehicleId != null) {
            shipments = shipments.stream()
                    .filter(s -> s.getVehicle() != null && s.getVehicle().getId().equals(vehicleId))
                    .collect(Collectors.toList());
        }

        if (startDateStr != null && !startDateStr.isBlank()) {
            LocalDate start = LocalDate.parse(startDateStr);
            shipments = shipments.stream()
                    .filter(s -> s.getDateOfReceipt() != null && !s.getDateOfReceipt().toLocalDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDateStr != null && !endDateStr.isBlank()) {
            LocalDate end = LocalDate.parse(endDateStr);
            shipments = shipments.stream()
                    .filter(s -> s.getDateOfReceipt() != null && !s.getDateOfReceipt().toLocalDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Danh Sách Đơn Hàng");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Title Row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO DÂN HÀNG VẬN CHUYỂN (TMS-01)");
            titleCell.setCellStyle(headerStyle);

            // Headers
            String[] headers = {"STT", "MÃ ĐƠN HÀNG", "KHÁCH HÀNG", "NƠI NHẬN", "NƠI GIAO", "TẢI TRỌNG (TẤN)", "NGÀY NHẬN", "NGÀY GIAO", "DOANH THU (VND)", "TRẠNG THÁI"};
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowIdx = 3;
            for (int i = 0; i < shipments.size(); i++) {
                Shipment s = shipments.get(i);
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(s.getShipmentCode() != null ? s.getShipmentCode() : "");
                row.createCell(2).setCellValue(s.getCustomer() != null ? (s.getCustomer().getCompanyName() != null ? s.getCustomer().getCompanyName() : s.getCustomer().getFirstname() + " " + s.getCustomer().getLastname()) : "");
                row.createCell(3).setCellValue(s.getReceiptPlace() != null ? s.getReceiptPlace() : "");
                row.createCell(4).setCellValue(s.getDeliveryPlace() != null ? s.getDeliveryPlace() : "");
                row.createCell(5).setCellValue(s.getWeight() != null ? s.getWeight() : 0);
                row.createCell(6).setCellValue(s.getDateOfReceipt() != null ? s.getDateOfReceipt().format(DATE_FORMATTER) : "");
                row.createCell(7).setCellValue(s.getDeliveryDate() != null ? s.getDeliveryDate().format(DATE_FORMATTER) : "");

                Cell revCell = row.createCell(8);
                revCell.setCellValue(s.getRevenue() != null ? s.getRevenue().doubleValue() : 0.0);
                revCell.setCellStyle(currencyStyle);

                row.createCell(9).setCellValue(s.getStatusEnum() != null ? s.getStatusEnum().getStatusEnumName() : "CREATED");

                for (int j = 0; j <= 7; j++) {
                    if (j != 8) row.getCell(j).setCellStyle(dataStyle);
                }
                row.getCell(9).setCellStyle(dataStyle);
            }

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel Đơn hàng: ", e);
            throw new RuntimeException("Lỗi khi xuất file Excel đơn hàng: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportExpenses(Long vehicleId, String startDateStr, String endDateStr) {
        List<Expense> expenses = expenseRepository.findAll();

        if (vehicleId != null) {
            expenses = expenses.stream()
                    .filter(e -> e.getVehicle() != null && e.getVehicle().getId().equals(vehicleId))
                    .collect(Collectors.toList());
        }

        if (startDateStr != null && !startDateStr.isBlank()) {
            LocalDate start = LocalDate.parse(startDateStr);
            expenses = expenses.stream()
                    .filter(e -> e.getExpenseDate() != null && !e.getExpenseDate().toLocalDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDateStr != null && !endDateStr.isBlank()) {
            LocalDate end = LocalDate.parse(endDateStr);
            expenses = expenses.stream()
                    .filter(e -> e.getExpenseDate() != null && !e.getExpenseDate().toLocalDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Danh Sách Chi Phí");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            String[] headers = {"STT", "MÃ PHIẾU CHI", "TIÊU ĐỀ TRÍCH YẾU", "BIỂN SỐ XE", "NGÀY CHI", "TỔNG CHI PHÍ (VND)", "GHI CHÚ"};
            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 2;
            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.get(i);
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(e.getExpenseCode() != null ? e.getExpenseCode() : "");
                row.createCell(2).setCellValue(e.getTitle() != null ? e.getTitle() : "");
                row.createCell(3).setCellValue(e.getVehicleLicensePlate() != null ? e.getVehicleLicensePlate() : "");
                row.createCell(4).setCellValue(e.getExpenseDate() != null ? e.getExpenseDate().format(DATE_FORMATTER) : "");

                Cell expCell = row.createCell(5);
                expCell.setCellValue(e.getTotalExpense() != null ? e.getTotalExpense().doubleValue() : 0.0);
                expCell.setCellStyle(currencyStyle);

                row.createCell(6).setCellValue(e.getNotes() != null ? e.getNotes() : "");

                for (int j = 0; j <= 4; j++) row.getCell(j).setCellStyle(dataStyle);
                row.getCell(6).setCellStyle(dataStyle);
            }

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel Chi phí: ", e);
            throw new RuntimeException("Lỗi khi xuất file Excel chi phí: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportSalaries(Long employeeId, String startDateStr, String endDateStr) {
        List<Salary> salaries = salaryRepository.findAll();

        if (employeeId != null) {
            salaries = salaries.stream()
                    .filter(s -> s.getEmployee() != null && s.getEmployee().getEmployeeId().equals(employeeId))
                    .collect(Collectors.toList());
        }

        if (startDateStr != null && !startDateStr.isBlank()) {
            LocalDate start = LocalDate.parse(startDateStr);
            salaries = salaries.stream()
                    .filter(s -> s.getStartDate() != null && !s.getStartDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDateStr != null && !endDateStr.isBlank()) {
            LocalDate end = LocalDate.parse(endDateStr);
            salaries = salaries.stream()
                    .filter(s -> s.getEndDate() != null && !s.getEndDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bảng Lương Nhân Sự");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            String[] headers = {"STT", "MÃ LƯƠNG", "MÃ NV", "HỌ TÊN NHÂN VIÊN", "KỲ TÍNH LƯƠNG", "LƯƠNG CỨNG", "SỐ CHUYẾN", "LƯƠNG CHUYẾN", "PHỤ CẤP", "KHẤU TRỪ", "THỰC NHẬN (VND)"};
            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 2;
            for (int i = 0; i < salaries.size(); i++) {
                Salary s = salaries.get(i);
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(s.getSalaryCode() != null ? s.getSalaryCode() : "");
                row.createCell(2).setCellValue(s.getEmployeeCode() != null ? s.getEmployeeCode() : "");
                row.createCell(3).setCellValue(s.getEmployee() != null ? s.getEmployee().getFirstname() + " " + s.getEmployee().getLastname() : "");
                row.createCell(4).setCellValue((s.getStartDate() != null ? s.getStartDate().format(DATE_FORMATTER) : "") + " - " + (s.getEndDate() != null ? s.getEndDate().format(DATE_FORMATTER) : ""));

                createCurrencyCell(row, 5, s.getSalaryBasicCosts(), currencyStyle);
                row.createCell(6).setCellValue(s.getTotalShipmentCount() != null ? s.getTotalShipmentCount() : 0);
                createCurrencyCell(row, 7, s.getTotalSalaryPerShipment(), currencyStyle);
                createCurrencyCell(row, 8, s.getAllowanceCosts(), currencyStyle);
                createCurrencyCell(row, 9, s.getDeductionCosts(), currencyStyle);
                createCurrencyCell(row, 10, s.getSalaryCosts(), currencyStyle);

                for (int j = 0; j <= 4; j++) row.getCell(j).setCellStyle(dataStyle);
                row.getCell(6).setCellStyle(dataStyle);
            }

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel Bảng lương: ", e);
            throw new RuntimeException("Lỗi khi xuất file Excel bảng lương: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportSalaryById(Long salaryId) {
        Salary salary = salaryRepository.findBySalaryIdAndIsDeleteFalse(salaryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bảng lương với ID: " + salaryId));

        List<SalaryShipment> salaryShipments = salaryShipmentRepository.findBySalaryMain_SalaryId(salaryId);

        BigDecimal tripPct = salary.getTripSalaryPercentage() != null ? salary.getTripSalaryPercentage() : BigDecimal.ZERO;

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Phiếu Lương");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle sectionStyle = createSectionStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);

            // Title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("PHIẾU TÍNH LƯƠNG - " + salary.getSalaryCode());
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 6));

            // ===== PHẦN 1: THÔNG TIN CHUNG =====
            Row sec1Row = sheet.createRow(2);
            Cell sec1 = sec1Row.createCell(0);
            sec1.setCellValue("PHẦN 1 — THÔNG TIN PHIẾU LƯƠNG & TÀI XẾ");
            sec1.setCellStyle(sectionStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2, 2, 0, 6));

            String[][] info = {
                {"Mã Phiếu Lương:", salary.getSalaryCode()},
                {"Họ Tên Nhân Viên:", salary.getEmployee() != null ? salary.getEmployee().getFirstname() + " " + salary.getEmployee().getLastname() : ""},
                {"Mã Nhân Viên:", salary.getEmployeeCode() != null ? salary.getEmployeeCode() : ""},
                {"Kỳ Lương:", (salary.getStartDate() != null ? salary.getStartDate().format(DATE_FORMATTER) : "") + " — " + (salary.getEndDate() != null ? salary.getEndDate().format(DATE_FORMATTER) : "")},
                {"Số Ngày Công:", salary.getWorkDaysCount() != null ? String.valueOf(salary.getWorkDaysCount()) : "0"},
                {"Ghi Chú:", salary.getNotes() != null ? salary.getNotes() : ""},
            };
            int r = 3;
            for (String[] row : info) {
                Row ir = sheet.createRow(r++);
                Cell lbl = ir.createCell(0); lbl.setCellValue(row[0]); lbl.setCellStyle(headerStyle);
                Cell val = ir.createCell(1); val.setCellValue(row[1]); val.setCellStyle(dataStyle);
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(r-1, r-1, 1, 6));
            }

            // ===== PHẦN 2: DANH SÁCH CHUYẾN HÀNG =====
            int sec2Start = r + 1;
            Row sec2Row = sheet.createRow(sec2Start);
            Cell sec2 = sec2Row.createCell(0);
            sec2.setCellValue("PHẦN 2 — CHUYẾN HÀNG CỦA TÀI XẾ TRONG KỲ LƯƠNG (" + salaryShipments.size() + " chuyến)");
            sec2.setCellStyle(sectionStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(sec2Start, sec2Start, 0, 6));

            String[] shipHeaders = {"STT", "MÃ ĐƠN HÀNG", "NƠI NHẬN", "NƠI GIAO", "NGÀY NHẬN", "NGÀY GIAO",
                    "DOANH THU CƯỚC (VND)"};
            Row shipHeaderRow = sheet.createRow(sec2Start + 1);
            for (int i = 0; i < shipHeaders.length; i++) {
                Cell c = shipHeaderRow.createCell(i);
                c.setCellValue(shipHeaders[i]);
                c.setCellStyle(headerStyle);
            }

            int shipRowIdx = sec2Start + 2;
            CellStyle altRowStyle   = createAltRowStyle(workbook);
            CellStyle altCurrStyle  = createAltCurrencyStyle(workbook);
            for (int i = 0; i < salaryShipments.size(); i++) {
                Shipment s = salaryShipments.get(i).getShipment();
                Row row = sheet.createRow(shipRowIdx++);
                boolean alt = (i % 2 == 1);
                CellStyle rowDs = alt ? altRowStyle  : dataStyle;
                CellStyle rowCs = alt ? altCurrStyle : currencyStyle;
                row.createCell(0).setCellValue(i + 1); row.getCell(0).setCellStyle(rowDs);
                row.createCell(1).setCellValue(s.getShipmentCode() != null ? s.getShipmentCode() : ""); row.getCell(1).setCellStyle(rowDs);
                row.createCell(2).setCellValue(s.getReceiptPlace() != null ? s.getReceiptPlace() : "-"); row.getCell(2).setCellStyle(rowDs);
                row.createCell(3).setCellValue(s.getDeliveryPlace() != null ? s.getDeliveryPlace() : "-"); row.getCell(3).setCellStyle(rowDs);
                row.createCell(4).setCellValue(s.getDateOfReceipt() != null ? s.getDateOfReceipt().format(DATE_FORMATTER) : "-"); row.getCell(4).setCellStyle(rowDs);
                row.createCell(5).setCellValue(s.getDeliveryDate() != null ? s.getDeliveryDate().format(DATE_FORMATTER) : "-"); row.getCell(5).setCellStyle(rowDs);
                // Doanh thu cước = doanh thu x tỷ lệ % hoa hồng
                BigDecimal rawRevenue = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal commission = rawRevenue.multiply(tripPct).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
                Cell revCell = row.createCell(6);
                revCell.setCellValue(commission.doubleValue());
                revCell.setCellStyle(rowCs);
            }

            // ===== PHẦN 3: TỔNG KẾT LƯƠNG =====
            int sec3Start = shipRowIdx + 1;
            Row sec3Row = sheet.createRow(sec3Start);
            Cell sec3 = sec3Row.createCell(0);
            sec3.setCellValue("PHẦN 3 — TỔNG KẾT LƯƠNG THEO KỲ");
            sec3.setCellStyle(sectionStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(sec3Start, sec3Start, 0, 6));

            CellStyle deductStyle = createDeductStyle(workbook);
            Object[][] salaryRows = {
                {"Lương Cứng Định Kỳ:",               salary.getSalaryBasicCosts(),    currencyStyle},
                {"Lương Chuyến (" + salary.getTotalShipmentCount() + " chuyến):", salary.getTotalSalaryPerShipment(), currencyStyle},
                {"Phụ Cấp Ăn Uống / Lưu Đêm (+):",   salary.getAllowanceCosts(),       currencyStyle},
                {"Khấu Trừ / Vi Phạm (-):",            salary.getDeductionCosts(),      deductStyle},
            };
            int totalRowIdx = sec3Start + 1;
            for (Object[] row : salaryRows) {
                Row tr = sheet.createRow(totalRowIdx++);
                Cell lbl = tr.createCell(0); lbl.setCellValue((String)row[0]); lbl.setCellStyle(createSummaryLabelStyle(workbook));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(totalRowIdx-1, totalRowIdx-1, 0, 5));
                Cell val = tr.createCell(6);
                val.setCellValue(row[1] instanceof BigDecimal ? ((BigDecimal)row[1]).doubleValue() : 0.0);
                val.setCellStyle((CellStyle)row[2]);
            }

            Row grandTotalRow = sheet.createRow(totalRowIdx);
            Cell gtLabel = grandTotalRow.createCell(0);
            gtLabel.setCellValue("→ TỔNG LƯƠNG THỰC NHẬN:");
            gtLabel.setCellStyle(totalStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(totalRowIdx, totalRowIdx, 0, 5));
            Cell gtVal = grandTotalRow.createCell(6);
            gtVal.setCellValue(salary.getSalaryCosts() != null ? salary.getSalaryCosts().doubleValue() : 0.0);
            gtVal.setCellStyle(totalStyle);

            for (int i = 0; i <= 6; i++) sheet.autoSizeColumn(i);
            sheet.setColumnWidth(2, 9000); // NƠI NHẬN
            sheet.setColumnWidth(3, 9000); // NƠI GIAO

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel Bảng lương theo ID [{}]: ", salaryId, e);
            throw new RuntimeException("Lỗi khi xuất file Excel bảng lương: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportRevenues(Long vehicleId, String startDateStr, String endDateStr) {
        List<RevenueFinal> revenues = revenueFinalRepository.findAll();

        if (startDateStr != null && !startDateStr.isBlank()) {
            LocalDate start = LocalDate.parse(startDateStr);
            revenues = revenues.stream()
                    .filter(r -> r.getStartDate() != null && !r.getStartDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDateStr != null && !endDateStr.isBlank()) {
            LocalDate end = LocalDate.parse(endDateStr);
            revenues = revenues.stream()
                    .filter(r -> r.getEndDate() != null && !r.getEndDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Báo Cáo Doanh Thu");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            String[] headers = {"STT", "MÃ CHỨNG TỪ", "TỪ NGÀY", "ĐẾN NGÀY", "SỐ CHUYẾN", "TỔNG CHI PHÍ (VND)", "TỔNG LƯƠNG (VND)", "DOANH THU THUẦN (LỢI NHUẬN)"};
            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 2;
            for (int i = 0; i < revenues.size(); i++) {
                RevenueFinal r = revenues.get(i);
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(r.getRevenueCode() != null ? r.getRevenueCode() : "");
                row.createCell(2).setCellValue(r.getStartDate() != null ? r.getStartDate().format(DATE_FORMATTER) : "");
                row.createCell(3).setCellValue(r.getEndDate() != null ? r.getEndDate().format(DATE_FORMATTER) : "");
                row.createCell(4).setCellValue(r.getTotalShipment() != null ? r.getTotalShipment() : 0);

                createCurrencyCell(row, 5, r.getTotalExpense(), currencyStyle);
                createCurrencyCell(row, 6, r.getTotalSalary(), currencyStyle);
                createCurrencyCell(row, 7, r.getRevenueFinalCosts(), currencyStyle);

                for (int j = 0; j <= 4; j++) row.getCell(j).setCellStyle(dataStyle);
            }

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Lỗi khi xuất Excel Doanh thu: ", e);
            throw new RuntimeException("Lỗi khi xuất file Excel doanh thu: " + e.getMessage());
        }
    }

    // ==================== HELPER: XSSFColor ====================
    private org.apache.poi.xssf.usermodel.XSSFColor rgb(byte r, byte g, byte b) {
        return new org.apache.poi.xssf.usermodel.XSSFColor(new byte[]{r, g, b}, null);
    }

    /** Tiêu đề file — chữ xanh da trời đậm, cỡ lớn */
    private CellStyle createTitleStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(rgb((byte)0x0D, (byte)0x47, (byte)0xA1)); // xanh da trời đậm
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /** Header cột — nền xanh da trời sáng #1565C0, chữ trắng */
    private CellStyle createHeaderStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(rgb((byte)0x15, (byte)0x65, (byte)0xC0)); // #1565C0 xanh da trời
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    /** Header section (PHẦN 1,2,3) — nền vàng cam #F57F17, chữ trắng */
    private CellStyle createSectionStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(rgb((byte)0xF5, (byte)0x7F, (byte)0x17)); // #F57F17 vàng cam
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /** Nhãn tổng kết lương — nền xám nhạt #ECEFF1, chữ đen đậm */
    private CellStyle createSummaryLabelStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(rgb((byte)0xEC, (byte)0xEF, (byte)0xF1)); // xám rất nhạt
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    /** Dòng tổng lương thực nhận — nền xanh lá nhạt #2E7D32, chữ trắng, số tiền */
    private CellStyle createTotalStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(rgb((byte)0x2E, (byte)0x7D, (byte)0x32)); // #2E7D32 xanh lá đậm
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0 \"VND\""));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    /** Dữ liệu bình thường — viền mỏng, nền trắng */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    /** Số tiền bình thường — căn phải, định dạng VND */
    private CellStyle createCurrencyStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(rgb((byte)0x1B, (byte)0x5E, (byte)0x20)); // xanh lá đậm
        style.setFont(font);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0 \"VND\""));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    /** Số tiền khấu trừ — màu đỏ */
    private CellStyle createDeductStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(rgb((byte)0xB7, (byte)0x1C, (byte)0x1C)); // đỏ đậm
        style.setFont(font);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0 \"VND\""));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    /** Dữ liệu dòng xen kẽ — nền xanh da trời rất nhạt #E3F2FD */
    private CellStyle createAltRowStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        style.setFillForegroundColor(rgb((byte)0xE3, (byte)0xF2, (byte)0xFD)); // #E3F2FD xanh nhạt
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    /** Số tiền dòng xen kẽ — nền xanh nhạt + chữ xanh lá */
    private CellStyle createAltCurrencyStyle(Workbook workbook) {
        org.apache.poi.xssf.usermodel.XSSFCellStyle style =
                (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont font =
                (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(rgb((byte)0x1B, (byte)0x5E, (byte)0x20));
        style.setFont(font);
        style.setFillForegroundColor(rgb((byte)0xE3, (byte)0xF2, (byte)0xFD));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0 \"VND\""));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        return style;
    }

    private void setBorderThin(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private void createCurrencyCell(Row row, int col, BigDecimal val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val != null ? val.doubleValue() : 0.0);
        cell.setCellStyle(style);
    }

    private CellStyle createDataStyleRight(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }
}
