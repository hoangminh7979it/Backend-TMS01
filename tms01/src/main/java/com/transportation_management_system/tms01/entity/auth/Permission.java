package com.transportation_management_system.tms01.entity.auth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "permission_code", nullable = false, unique = true, length = 100)
    private String permissionCode;

    @Column(name = "permission_name", nullable = false, length = 150)
    private String permissionName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
