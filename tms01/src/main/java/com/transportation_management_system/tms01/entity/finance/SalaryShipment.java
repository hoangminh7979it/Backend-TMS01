package com.transportation_management_system.tms01.entity.finance;

import com.transportation_management_system.tms01.entity.shipment.Shipment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_salary_shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_shipment_id")
    private Long salaryShipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_main_id", nullable = false)
    private Salary salaryMain;
}
