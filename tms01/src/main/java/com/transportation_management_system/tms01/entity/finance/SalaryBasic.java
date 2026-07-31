package com.transportation_management_system.tms01.entity.finance;

import com.transportation_management_system.tms01.entity.hrm.EmployeeType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tms_salary_basics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_basic_id")
    private Long salaryBasicId;

    @Column(name = "salary_basic_code", nullable = false, unique = true, length = 50)
    private String salaryBasicCode;

    @Column(name = "salary_basic_costs", precision = 18, scale = 2)
    private BigDecimal salaryBasicCosts;

    @Column(name = "salary_basic_per_shipment", precision = 18, scale = 2)
    private BigDecimal salaryBasicPerShipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_type_id")
    private EmployeeType employeeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_basic_type_id")
    private SalaryBasicType salaryBasicType;
}
