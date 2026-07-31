package com.transportation_management_system.tms01.repository.fleet;

import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlateAndIsDeleteFalse(String licensePlate);

    Optional<Vehicle> findByIdAndIsDeleteFalse(Long id);

    List<Vehicle> findAllByIsDeleteFalse();

    List<Vehicle> findByVehicleType_VehicleTypeCodeAndIsDeleteFalse(String typeCode);

    List<Vehicle> findByStatusAndIsDeleteFalse(String status);

    boolean existsByLicensePlate(String licensePlate);
}
