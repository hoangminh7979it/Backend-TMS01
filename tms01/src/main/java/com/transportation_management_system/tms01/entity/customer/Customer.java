package com.transportation_management_system.tms01.entity.customer;

import com.transportation_management_system.tms01.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tms_customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_code", nullable = false, unique = true, length = 50)
    private String customerCode;

    @Column(name = "firstname", length = 100)
    private String firstname;

    @Column(name = "lastname", length = 100)
    private String lastname;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "tax_code", length = 50)
    private String taxCode; // Mã số thuế

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "customer_type", length = 50)
    private String customerType; // CORPORATE, INDIVIDUAL

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
