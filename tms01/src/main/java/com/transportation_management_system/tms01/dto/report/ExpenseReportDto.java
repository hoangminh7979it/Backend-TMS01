package com.transportation_management_system.tms01.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseReportDto {
    private String expenseCode;
    private String title;
    private String licensePlate;
    private String expenseTypeName;
    private String expenseDate;
    private BigDecimal totalExpense;
    private String notes;
}
