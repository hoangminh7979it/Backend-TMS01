package com.transportation_management_system.tms01.repository.auth;

import com.transportation_management_system.tms01.entity.auth.SystemFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemFeatureRepository extends JpaRepository<SystemFeature, Long> {

    Optional<SystemFeature> findByFeatureCode(String featureCode);

    boolean existsByFeatureCode(String featureCode);

    List<SystemFeature> findByIsActiveTrueOrderBySortOrderAsc();

    List<SystemFeature> findAllByOrderBySortOrderAsc();
}
