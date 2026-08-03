package com.transportation_management_system.tms01.repository.revenue;

import com.transportation_management_system.tms01.entity.revenue.RevenueFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueFinalRepository extends JpaRepository<RevenueFinal, Long> {

    Optional<RevenueFinal> findByRevenueCodeAndIsDeleteFalse(String revenueCode);

    Optional<RevenueFinal> findByRevenueIdAndIsDeleteFalse(Long revenueId);

    List<RevenueFinal> findAllByIsDeleteFalse();

    boolean existsByRevenueCode(String revenueCode);
}
