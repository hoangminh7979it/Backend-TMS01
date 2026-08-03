package com.transportation_management_system.tms01.entity.expense;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tms_expense_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExpenseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_detail_id")
    private Long expenseDetailId;

    @Column(name = "expense_detail_code", nullable = false, unique = true, length = 50)
    private String expenseDetailCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_type_id")
    private ExpenseType expenseType;

    @Column(name = "expense_type_code", length = 50)
    private String expenseTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name = "expense_detail_costs", precision = 18, scale = 2)
    private BigDecimal expenseDetailCosts;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date")
    private LocalDate date;
}
