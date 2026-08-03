package com.transportation_management_system.tms01.entity.revenue;

import com.transportation_management_system.tms01.entity.shipment.Shipment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tms_revenue_shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RevenueShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_shipment_id")
    private Long revenueShipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revenue_id", nullable = false)
    private RevenueFinal revenueFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;
}
