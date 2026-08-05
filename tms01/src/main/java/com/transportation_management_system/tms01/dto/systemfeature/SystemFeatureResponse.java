package com.transportation_management_system.tms01.dto.systemfeature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemFeatureResponse {

    private Long featureId;
    private String featureCode;
    private String featureName;
    private String routePath;
    private String iconClass;
    private String resourceGroup;
    private Integer sortOrder;
    private Boolean isActive;
    private String description;
}
