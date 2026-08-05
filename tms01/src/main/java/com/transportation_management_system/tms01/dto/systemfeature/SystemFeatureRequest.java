package com.transportation_management_system.tms01.dto.systemfeature;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SystemFeatureRequest {

    @NotBlank(message = "Mã tính năng không được để trống")
    private String featureCode;

    @NotBlank(message = "Tên tính năng không được để trống")
    private String featureName;

    private String routePath;
    private String iconClass;
    private String resourceGroup;
    private Integer sortOrder;
    private Boolean isActive;
    private String description;
}
