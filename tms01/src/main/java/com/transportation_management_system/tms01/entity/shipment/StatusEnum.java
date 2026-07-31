package com.transportation_management_system.tms01.entity.shipment;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_status_enums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusEnum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_enum_id")
    private Long statusEnumId;

    @Column(name = "status_enum_code", nullable = false, unique = true, length = 50)
    private String statusEnumCode;

    @Column(name = "status_enum_name", nullable = false, length = 100)
    private String statusEnumName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
