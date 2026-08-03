package com.transportation_management_system.tms01.entity.expense;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tms_expense_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExpenseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_type_id")
    private Long expenseTypeId;

    @Column(name = "expense_type_code", nullable = false, unique = true, length = 50)
    private String expenseTypeCode;

    @Column(name = "expense_type_name", nullable = false, length = 100)
    private String expenseTypeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
