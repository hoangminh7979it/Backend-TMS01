package com.transportation_management_system.tms01.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTypeResponse {

    private Long expenseTypeId;
    private String expenseTypeCode;
    private String expenseTypeName;
    private String description;
}
