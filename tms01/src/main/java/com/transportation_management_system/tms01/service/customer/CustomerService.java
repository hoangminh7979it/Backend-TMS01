package com.transportation_management_system.tms01.service.customer;

import com.transportation_management_system.tms01.dto.customer.CompanyRequest;
import com.transportation_management_system.tms01.dto.customer.CompanyResponse;
import com.transportation_management_system.tms01.dto.customer.CustomerRequest;
import com.transportation_management_system.tms01.dto.customer.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> getAllCustomers();

    List<CustomerResponse> getCustomersByType(String customerType);

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(Long id, CompanyRequest request);

    void deleteCompany(Long id);

    List<CompanyResponse> getAllCompanies();

    List<CompanyResponse> getCompaniesByCustomerId(Long customerId);
}
