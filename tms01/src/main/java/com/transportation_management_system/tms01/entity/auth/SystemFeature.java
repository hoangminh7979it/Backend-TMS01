package com.transportation_management_system.tms01.entity.auth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_system_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private Long featureId;

    @Column(name = "feature_code", nullable = false, unique = true, length = 100)
    private String featureCode;

    @Column(name = "feature_name", nullable = false, length = 150)
    private String featureName;

    @Column(name = "route_path", length = 255)
    private String routePath;

    @Column(name = "icon_class", length = 100)
    private String iconClass;

    @Column(name = "resource_group", length = 100)
    private String resourceGroup;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
