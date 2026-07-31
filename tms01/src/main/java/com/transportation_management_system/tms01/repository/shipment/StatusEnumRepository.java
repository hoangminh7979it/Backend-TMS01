package com.transportation_management_system.tms01.repository.shipment;

import com.transportation_management_system.tms01.entity.shipment.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusEnumRepository extends JpaRepository<StatusEnum, Long> {

    Optional<StatusEnum> findByStatusEnumCode(String statusEnumCode);

    boolean existsByStatusEnumCode(String statusEnumCode);
}
