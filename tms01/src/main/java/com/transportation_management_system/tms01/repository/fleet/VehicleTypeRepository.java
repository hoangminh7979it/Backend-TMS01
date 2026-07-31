package com.transportation_management_system.tms01.repository.fleet;

import com.transportation_management_system.tms01.entity.fleet.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    Optional<VehicleType> findByVehicleTypeCode(String vehicleTypeCode);

    boolean existsByVehicleTypeCode(String vehicleTypeCode);
}
