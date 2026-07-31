package com.transportation_management_system.tms01.entity.finance;

import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tms_revenue_finals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueFinal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_id")
    private Long revenueId;

    @Column(name = "revenue_code", nullable = false, unique = true, length = 50)
    private String revenueCode;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_shipment")
    private Integer totalShipment;

    @Column(name = "total_expense", precision = 18, scale = 2)
    private BigDecimal totalExpense;

    @Column(name = "total_salary", precision = 18, scale = 2)
    private BigDecimal totalSalary;

    @Column(name = "revenue_final_costs", precision = 18, scale = 2)
    private BigDecimal revenueFinalCosts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_enum_id")
    private StatusEnum statusEnum;

    @Column(name = "status_enum_code", length = 50)
    private String statusEnumCode;
}
