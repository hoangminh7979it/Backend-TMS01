package com.transportation_management_system.tms01.service.auth;

import com.transportation_management_system.tms01.dto.systemfeature.SystemFeatureRequest;
import com.transportation_management_system.tms01.dto.systemfeature.SystemFeatureResponse;

import java.util.List;

public interface SystemFeatureService {

    List<SystemFeatureResponse> getAllFeatures();

    List<SystemFeatureResponse> getActiveFeatures();

    SystemFeatureResponse getFeatureById(Long featureId);

    SystemFeatureResponse createFeature(SystemFeatureRequest request);

    SystemFeatureResponse updateFeature(Long featureId, SystemFeatureRequest request);

    void deleteFeature(Long featureId);

    SystemFeatureResponse toggleActiveStatus(Long featureId);
}
