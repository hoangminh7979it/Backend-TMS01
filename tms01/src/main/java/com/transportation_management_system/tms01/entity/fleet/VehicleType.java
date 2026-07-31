package com.transportation_management_system.tms01.entity.fleet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_vehicle_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_type_id")
    private Long vehicleTypeId;

    @Column(name = "vehicle_type_code", nullable = false, unique = true, length = 50)
    private String vehicleTypeCode;

    @Column(name = "vehicle_type_name", nullable = false, length = 100)
    private String vehicleTypeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
