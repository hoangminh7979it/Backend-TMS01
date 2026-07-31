package com.transportation_management_system.tms01.entity.finance;

import com.transportation_management_system.tms01.entity.shipment.Shipment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tms_revenue_shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
