package com.transportation_management_system.tms01.entity.hrm;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_employee_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_type_id")
    private Long employeeTypeId;

    @Column(name = "employee_type_code", nullable = false, unique = true, length = 50)
    private String employeeTypeCode;

    @Column(name = "employee_type_name", nullable = false, length = 100)
    private String employeeTypeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
