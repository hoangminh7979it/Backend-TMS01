package com.transportation_management_system.tms01.repository.customer;

import com.transportation_management_system.tms01.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerCodeAndIsDeleteFalse(String customerCode);

    Optional<Customer> findByCustomerIdAndIsDeleteFalse(Long customerId);

    List<Customer> findAllByIsDeleteFalse();

    List<Customer> findByCustomerTypeAndIsDeleteFalse(String customerType);

    boolean existsByCustomerCode(String customerCode);
}
