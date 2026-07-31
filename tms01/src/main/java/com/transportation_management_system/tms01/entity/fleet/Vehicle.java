package com.transportation_management_system.tms01.entity.fleet;

import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "tms_vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Vehicle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vehicle_code", length = 50)
    private String vehicleCode;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "license_plate", nullable = false, unique = true, length = 50)
    private String licensePlate;

    @Column(name = "payload_capacity")
    private Double payloadCapacity; // Tải trọng thiết kế (Tấn)

    @Column(name = "status", length = 50)
    private String status; // AVAILABLE, IN_TRANSIT, MAINTENANCE

    @Column(name = "inspection_expiration_date")
    private LocalDate inspectionExpirationDate; // Hạn đăng kiểm

    @Column(name = "insurance_expiration_date")
    private LocalDate insuranceExpirationDate; // Hạn bảo hiểm

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee; // Lái xe phụ trách

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;
}
