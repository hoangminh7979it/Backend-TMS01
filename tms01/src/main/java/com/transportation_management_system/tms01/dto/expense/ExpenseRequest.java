package com.transportation_management_system.tms01.dto.expense;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {

    @NotBlank(message = "Mã phiếu chi phí không được để trống")
    private String expenseCode;

    private String title;
    private LocalDateTime expenseDate;
    private BigDecimal totalExpense;
    private String vehicleLicensePlate;
    private Long vehicleId;
    private Long shipmentId;
    private Long employeeId; // Lái xe / Người thanh toán
    private Long statusEnumId;
    private String notes;

    private List<ExpenseDetailRequest> details;
}
