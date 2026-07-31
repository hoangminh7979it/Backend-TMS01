package com.transportation_management_system.tms01.entity.shipment;

import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.base.BaseEntity;
import com.transportation_management_system.tms01.entity.customer.Customer;
import com.transportation_management_system.tms01.entity.fleet.Vehicle;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tms_shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "shipment_code", nullable = false, unique = true, length = 50)
    private String shipmentCode;

    @Column(name = "cargo_type", length = 150)
    private String cargoType; // Loại hàng hóa (Ví dụ: Thiết bị điện tử, Nông sản, Thép cuộn...)

    @Column(name = "receipt_place", columnDefinition = "TEXT")
    private String receiptPlace;

    @Column(name = "delivery_place", columnDefinition = "TEXT")
    private String deliveryPlace;

    @Column(name = "weight")
    private Double weight; // Khối lượng (Tấn)

    @Column(name = "date_of_receipt")
    private LocalDateTime dateOfReceipt;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "revenue", precision = 18, scale = 2)
    private BigDecimal revenue;

    @Column(name = "incurred_costs", precision = 18, scale = 2)
    private BigDecimal incurredCosts;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee; // Lái xe phụ trách

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_enum_id")
    private StatusEnum statusEnum;
}
