package com.transportation_management_system.tms01.repository.shipment;

import com.transportation_management_system.tms01.entity.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    // === DATE RANGE QUERIES FOR PAYROLL - avoids PostgreSQL null type inference issue ===

    /** Lấy chuyến của tài xế khi có cả startDate và endDate */
    @Query("SELECT s FROM Shipment s WHERE s.isDelete = false " +
           "AND s.employee.employeeId = :employeeId " +
           "AND s.dateOfReceipt >= :startDate " +
           "AND s.dateOfReceipt <= :endDate " +
           "ORDER BY s.dateOfReceipt ASC")
    List<Shipment> findByEmployeeAndBothDates(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /** Lấy chuyến của tài xế khi chỉ có startDate */
    @Query("SELECT s FROM Shipment s WHERE s.isDelete = false " +
           "AND s.employee.employeeId = :employeeId " +
           "AND s.dateOfReceipt >= :startDate " +
           "ORDER BY s.dateOfReceipt ASC")
    List<Shipment> findByEmployeeAndStartDate(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDateTime startDate
    );

    /** Lấy chuyến của tài xế khi chỉ có endDate */
    @Query("SELECT s FROM Shipment s WHERE s.isDelete = false " +
           "AND s.employee.employeeId = :employeeId " +
           "AND s.dateOfReceipt <= :endDate " +
           "ORDER BY s.dateOfReceipt ASC")
    List<Shipment> findByEmployeeAndEndDate(
            @Param("employeeId") Long employeeId,
            @Param("endDate") LocalDateTime endDate
    );

    /** Lấy tất cả chuyến của tài xế khi không có filter ngày */
    @Query("SELECT s FROM Shipment s WHERE s.isDelete = false " +
           "AND s.employee.employeeId = :employeeId " +
           "ORDER BY s.dateOfReceipt ASC")
    List<Shipment> findByEmployeeId(@Param("employeeId") Long employeeId);
}
