package com.transportation_management_system.tms01.repository.customer;

import com.transportation_management_system.tms01.entity.customer.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCompanyIdAndIsDeleteFalse(Long companyId);

    List<Company> findAllByIsDeleteFalse();

    List<Company> findByCustomer_CustomerIdAndIsDeleteFalse(Long customerId);

    boolean existsByCompanyCode(String companyCode);
}
