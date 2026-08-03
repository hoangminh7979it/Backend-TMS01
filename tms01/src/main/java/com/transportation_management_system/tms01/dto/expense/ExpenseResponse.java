package com.transportation_management_system.tms01.dto.expense;

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
public class ExpenseResponse {

    private Long expenseId;
    private String expenseCode;
    private String title;
    private LocalDateTime expenseDate;
    private BigDecimal totalExpense;
    private String vehicleLicensePlate;
    private Long vehicleId;
    private String vehicleName;
    private Long shipmentId;
    private String shipmentCode;
    private Long employeeId;
    private String employeeName;
    private Long statusEnumId;
    private String statusEnumCode;
    private String statusEnumName;
    private String notes;

    private LocalDateTime createDate;

    private List<ExpenseDetailResponse> details;
}
