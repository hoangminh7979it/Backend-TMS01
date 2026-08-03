package com.transportation_management_system.tms01.repository.revenue;

import com.transportation_management_system.tms01.entity.revenue.RevenueShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueShipmentRepository extends JpaRepository<RevenueShipment, Long> {

    List<RevenueShipment> findByRevenueFinal_RevenueId(Long revenueId);

    void deleteByRevenueFinal_RevenueId(Long revenueId);
}
