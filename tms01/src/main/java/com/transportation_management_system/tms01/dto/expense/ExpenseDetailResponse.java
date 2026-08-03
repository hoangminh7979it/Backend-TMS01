package com.transportation_management_system.tms01.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDetailResponse {

    private Long expenseDetailId;
    private String expenseDetailCode;
    private Long expenseTypeId;
    private String expenseTypeCode;
    private String expenseTypeName;
    private BigDecimal expenseDetailCosts;
    private String description;
    private LocalDate date;
}
