package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.customer.CompanyRequest;
import com.transportation_management_system.tms01.dto.customer.CompanyResponse;
import com.transportation_management_system.tms01.dto.customer.CustomerRequest;
import com.transportation_management_system.tms01.dto.customer.CustomerResponse;
import com.transportation_management_system.tms01.entity.customer.Company;
import com.transportation_management_system.tms01.entity.customer.Customer;
import com.transportation_management_system.tms01.repository.customer.CompanyRepository;
import com.transportation_management_system.tms01.repository.customer.CustomerRepository;
import com.transportation_management_system.tms01.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByCustomerCode(request.getCustomerCode())) {
            throw new IllegalArgumentException("Mã khách hàng '" + request.getCustomerCode() + "' đã tồn tại trong hệ thống");
        }

        Customer customer = Customer.builder()
                .customerCode(request.getCustomerCode())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .companyName(request.getCompanyName())
                .taxCode(request.getTaxCode())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .customerType(request.getCustomerType() != null ? request.getCustomerType() : "CORPORATE")
                .notes(request.getNotes())
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        customer = customerRepository.save(customer);
        return mapToCustomerResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findByCustomerIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ khách hàng với ID: " + id));

        if (request.getFirstname() != null) customer.setFirstname(request.getFirstname());
        if (request.getLastname() != null) customer.setLastname(request.getLastname());
        if (request.getCompanyName() != null) customer.setCompanyName(request.getCompanyName());
        if (request.getTaxCode() != null) customer.setTaxCode(request.getTaxCode());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getCustomerType() != null) customer.setCustomerType(request.getCustomerType());
        if (request.getNotes() != null) customer.setNotes(request.getNotes());

        customerRepository.save(customer);
        return mapToCustomerResponse(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByCustomerIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ khách hàng với ID: " + id));

        customer.setIsDelete(true);
        customer.setDeleteDate(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findByCustomerIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ khách hàng với ID: " + id));
        return mapToCustomerResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapToCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByType(String customerType) {
        return customerRepository.findByCustomerTypeAndIsDeleteFalse(customerType).stream()
                .map(this::mapToCustomerResponse)
                .collect(Collectors.toList());
    }

    // --- COMPANY APIS ---

    @Override
    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        if (request.getCompanyCode() != null && companyRepository.existsByCompanyCode(request.getCompanyCode())) {
            throw new IllegalArgumentException("Mã công ty '" + request.getCompanyCode() + "' đã tồn tại");
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findByCustomerIdAndIsDeleteFalse(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
        }

        Company company = Company.builder()
                .companyCode(request.getCompanyCode())
                .name(request.getName())
                .taxCode(request.getTaxCode())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .customer(customer)
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        company = companyRepository.save(company);
        return mapToCompanyResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        Company company = companyRepository.findByCompanyIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công ty đối tác với ID: " + id));

        if (request.getName() != null) company.setName(request.getName());
        if (request.getTaxCode() != null) company.setTaxCode(request.getTaxCode());
        if (request.getContactPerson() != null) company.setContactPerson(request.getContactPerson());
        if (request.getEmail() != null) company.setEmail(request.getEmail());
        if (request.getPhone() != null) company.setPhone(request.getPhone());
        if (request.getAddress() != null) company.setAddress(request.getAddress());

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findByCustomerIdAndIsDeleteFalse(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
            company.setCustomer(customer);
        }

        companyRepository.save(company);
        return mapToCompanyResponse(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findByCompanyIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công ty đối tác với ID: " + id));

        company.setIsDelete(true);
        company.setDeleteDate(LocalDateTime.now());
        companyRepository.save(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapToCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getCompaniesByCustomerId(Long customerId) {
        return companyRepository.findByCustomer_CustomerIdAndIsDeleteFalse(customerId).stream()
                .map(this::mapToCompanyResponse)
                .collect(Collectors.toList());
    }

    private CustomerResponse mapToCustomerResponse(Customer c) {
        String fullName = (c.getFirstname() != null ? c.getFirstname() : "") + " " + (c.getLastname() != null ? c.getLastname() : "");
        return CustomerResponse.builder()
                .customerId(c.getCustomerId())
                .customerCode(c.getCustomerCode())
                .firstname(c.getFirstname())
                .lastname(c.getLastname())
                .fullName(fullName.trim())
                .companyName(c.getCompanyName())
                .taxCode(c.getTaxCode())
                .email(c.getEmail())
                .phone(c.getPhone())
                .address(c.getAddress())
                .customerType(c.getCustomerType())
                .notes(c.getNotes())
                .createDate(c.getCreateDate())
                .build();
    }

    private CompanyResponse mapToCompanyResponse(Company comp) {
        String customerName = null;
        if (comp.getCustomer() != null) {
            customerName = comp.getCustomer().getCompanyName() != null ? comp.getCustomer().getCompanyName() : comp.getCustomer().getFirstname();
        }

        return CompanyResponse.builder()
                .companyId(comp.getCompanyId())
                .companyCode(comp.getCompanyCode())
                .name(comp.getName())
                .taxCode(comp.getTaxCode())
                .contactPerson(comp.getContactPerson())
                .email(comp.getEmail())
                .phone(comp.getPhone())
                .address(comp.getAddress())
                .customerId(comp.getCustomer() != null ? comp.getCustomer().getCustomerId() : null)
                .customerName(customerName)
                .createDate(comp.getCreateDate())
                .build();
    }
}
