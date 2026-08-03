package com.transportation_management_system.tms01.entity.expense;

import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.entity.shipment.Shipment;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tms_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expenseId;

    @Column(name = "expense_code", nullable = false, unique = true, length = 50)
    private String expenseCode;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    @Column(name = "total_expense", precision = 18, scale = 2)
    private BigDecimal totalExpense;

    @Column(name = "vehicle_license_plate", length = 50)
    private String vehicleLicensePlate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee; // Người lập / Lái xe báo chi phí

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_enum_id")
    private StatusEnum statusEnum;
}
