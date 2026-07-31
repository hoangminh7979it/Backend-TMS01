package com.transportation_management_system.tms01.repository.shipment;

import com.transportation_management_system.tms01.entity.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentCodeAndIsDeleteFalse(String shipmentCode);

    Optional<Shipment> findByShipmentIdAndIsDeleteFalse(Long shipmentId);

    List<Shipment> findAllByIsDeleteFalse();

    List<Shipment> findByStatusEnum_StatusEnumCodeAndIsDeleteFalse(String statusCode);

    List<Shipment> findByCustomer_CustomerIdAndIsDeleteFalse(Long customerId);

    boolean existsByShipmentCode(String shipmentCode);
}
