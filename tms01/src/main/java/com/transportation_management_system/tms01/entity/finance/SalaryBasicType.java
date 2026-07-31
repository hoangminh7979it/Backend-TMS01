package com.transportation_management_system.tms01.entity.finance;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_salary_basic_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryBasicType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_basic_type_id")
    private Long salaryBasicTypeId;

    @Column(name = "salary_basic_type_code", nullable = false, unique = true, length = 50)
    private String salaryBasicTypeCode;

    @Column(name = "salary_basic_type_name", nullable = false, length = 100)
    private String salaryBasicTypeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
