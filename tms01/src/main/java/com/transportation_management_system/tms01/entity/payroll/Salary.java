package com.transportation_management_system.tms01.entity.payroll;

import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tms_salaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    @Column(name = "salary_basic_costs", precision = 18, scale = 2)
    private BigDecimal salaryBasicCosts;

    @Column(name = "work_days_count")
    private Integer workDaysCount; // Số ngày công làm việc

    @Column(name = "salary_basic_per_day", precision = 18, scale = 2)
    private BigDecimal salaryBasicPerDay; // Lương cứng theo ngày

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_shipment_count")
    private Integer totalShipmentCount;

    @Column(name = "driver_shipment_revenue", precision = 18, scale = 2)
    private BigDecimal driverShipmentRevenue; // Tổng doanh thu cước các chuyến tài xế đã thực hiện

    @Column(name = "trip_salary_percentage", precision = 5, scale = 2)
    private BigDecimal tripSalaryPercentage; // Tỷ lệ % thưởng theo doanh thu cước

    @Column(name = "total_salary_per_shipment", precision = 18, scale = 2)
    private BigDecimal totalSalaryPerShipment;

    @Column(name = "salary_costs", precision = 18, scale = 2)
    private BigDecimal salaryCosts;

    @Column(name = "allowance_costs", precision = 18, scale = 2)
    private BigDecimal allowanceCosts;

    @Column(name = "deduction_costs", precision = 18, scale = 2)
    private BigDecimal deductionCosts;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_enum_id")
    private StatusEnum statusEnum;
}
