package com.transportation_management_system.tms01.dto.expense;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTypeRequest {

    @NotBlank(message = "Mã loại chi phí không được để trống")
    private String expenseTypeCode;

    @NotBlank(message = "Tên loại chi phí không được để trống")
    private String expenseTypeName;

    private String description;
}
