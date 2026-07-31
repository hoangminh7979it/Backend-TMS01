package com.transportation_management_system.tms01.entity.finance;

import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tms_salaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long salaryId;

    @Column(name = "salary_code", nullable = false, unique = true, length = 50)
    private String salaryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "employee_code", length = 50)
    private String employeeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_basic_id")
    private SalaryBasic salaryBasic;

    @Column(name = "salary_basic_code", length = 50)
    private String salaryBasicCode;

    @Column(name = "salary_basic_costs", precision = 18, scale = 2)
    private BigDecimal salaryBasicCosts;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_salary_per_shipment", precision = 18, scale = 2)
    private BigDecimal totalSalaryPerShipment;

    @Column(name = "salary_costs", precision = 18, scale = 2)
    private BigDecimal salaryCosts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
