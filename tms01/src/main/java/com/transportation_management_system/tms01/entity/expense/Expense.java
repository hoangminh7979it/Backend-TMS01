package com.transportation_management_system.tms01.entity.expense;

import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tms_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expenseId;

    @Column(name = "expense_code", nullable = false, unique = true, length = 50)
    private String expenseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "vehicle_license_plate", length = 50)
    private String vehicleLicensePlate;

    @Column(name = "total_expense", precision = 18, scale = 2)
    private BigDecimal totalExpense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_enum_id")
    private StatusEnum statusEnum;
}
