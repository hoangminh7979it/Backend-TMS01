package com.transportation_management_system.tms01.service.impl;

import com.transportation_management_system.tms01.dto.hrm.EmployeeRequest;
import com.transportation_management_system.tms01.dto.hrm.EmployeeResponse;
import com.transportation_management_system.tms01.dto.hrm.EmployeeTypeRequest;
import com.transportation_management_system.tms01.dto.hrm.EmployeeTypeResponse;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.entity.hrm.Employee;
import com.transportation_management_system.tms01.entity.hrm.EmployeeType;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeRepository;
import com.transportation_management_system.tms01.repository.hrm.EmployeeTypeRepository;
import com.transportation_management_system.tms01.service.hrm.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Mã nhân viên '" + request.getEmployeeCode() + "' đã tồn tại trong hệ thống");
        }

        EmployeeType employeeType = null;
        if (request.getEmployeeTypeId() != null) {
            employeeType = employeeTypeRepository.findById(request.getEmployeeTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại nhân viên với ID: " + request.getEmployeeTypeId()));
        }

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản người dùng với ID: " + request.getUserId()));
        }

        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .nationalId(request.getNationalId())
                .drivingLicenseId(request.getDrivingLicenseId())
                .address(request.getAddress())
                .email(request.getEmail())
                .phone(request.getPhone())
                .employeeType(employeeType)
                .user(user)
                .isDelete(false)
                .createDate(LocalDateTime.now())
                .build();

        employee = employeeRepository.save(employee);
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ nhân viên với ID: " + id));

        if (request.getFirstname() != null) employee.setFirstname(request.getFirstname());
        if (request.getLastname() != null) employee.setLastname(request.getLastname());
        if (request.getNationalId() != null) employee.setNationalId(request.getNationalId());
        if (request.getDrivingLicenseId() != null) employee.setDrivingLicenseId(request.getDrivingLicenseId());
        if (request.getAddress() != null) employee.setAddress(request.getAddress());
        if (request.getEmail() != null) employee.setEmail(request.getEmail());
        if (request.getPhone() != null) employee.setPhone(request.getPhone());

        if (request.getEmployeeTypeId() != null) {
            EmployeeType type = employeeTypeRepository.findById(request.getEmployeeTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại nhân viên với ID: " + request.getEmployeeTypeId()));
            employee.setEmployeeType(type);
        }

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản người dùng với ID: " + request.getUserId()));
            employee.setUser(user);
        }

        employeeRepository.save(employee);
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ nhân viên với ID: " + id));

        employee.setIsDelete(true);
        employee.setDeleteDate(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findByEmployeeIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ nhân viên với ID: " + id));
        return mapToResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAllByIsDeleteFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByTypeCode(String typeCode) {
        return employeeRepository.findByEmployeeType_EmployeeTypeCodeAndIsDeleteFalse(typeCode).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeTypeResponse> getAllEmployeeTypes() {
        return employeeTypeRepository.findAll().stream()
                .map(this::mapToTypeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeTypeResponse createEmployeeType(EmployeeTypeRequest request) {
        if (employeeTypeRepository.existsByEmployeeTypeCode(request.getEmployeeTypeCode())) {
            throw new IllegalArgumentException("Mã loại nhân viên '" + request.getEmployeeTypeCode() + "' đã tồn tại");
        }

        EmployeeType type = EmployeeType.builder()
                .employeeTypeCode(request.getEmployeeTypeCode())
                .employeeTypeName(request.getEmployeeTypeName())
                .description(request.getDescription())
                .build();

        type = employeeTypeRepository.save(type);
        return mapToTypeResponse(type);
    }

    @Override
    @Transactional
    public EmployeeTypeResponse updateEmployeeType(Long id, EmployeeTypeRequest request) {
        EmployeeType type = employeeTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại nhân viên với ID: " + id));

        if (request.getEmployeeTypeName() != null) type.setEmployeeTypeName(request.getEmployeeTypeName());
        if (request.getDescription() != null) type.setDescription(request.getDescription());

        employeeTypeRepository.save(type);
        return mapToTypeResponse(type);
    }

    @Override
    @Transactional
    public void deleteEmployeeType(Long id) {
        EmployeeType type = employeeTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại nhân viên với ID: " + id));

        employeeTypeRepository.delete(type);
    }

    private EmployeeResponse mapToResponse(Employee emp) {
        String fullName = (emp.getFirstname() != null ? emp.getFirstname() : "") + " " + (emp.getLastname() != null ? emp.getLastname() : "");
        return EmployeeResponse.builder()
                .employeeId(emp.getEmployeeId())
                .employeeCode(emp.getEmployeeCode())
                .firstname(emp.getFirstname())
                .lastname(emp.getLastname())
                .fullName(fullName.trim())
                .nationalId(emp.getNationalId())
                .drivingLicenseId(emp.getDrivingLicenseId())
                .address(emp.getAddress())
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .employeeTypeId(emp.getEmployeeType() != null ? emp.getEmployeeType().getEmployeeTypeId() : null)
                .employeeTypeCode(emp.getEmployeeType() != null ? emp.getEmployeeType().getEmployeeTypeCode() : null)
                .employeeTypeName(emp.getEmployeeType() != null ? emp.getEmployeeType().getEmployeeTypeName() : null)
                .userId(emp.getUser() != null ? emp.getUser().getUserId() : null)
                .username(emp.getUser() != null ? emp.getUser().getUsername() : null)
                .createDate(emp.getCreateDate())
                .build();
    }

    private EmployeeTypeResponse mapToTypeResponse(EmployeeType type) {
        return EmployeeTypeResponse.builder()
                .employeeTypeId(type.getEmployeeTypeId())
                .employeeTypeCode(type.getEmployeeTypeCode())
                .employeeTypeName(type.getEmployeeTypeName())
                .description(type.getDescription())
                .build();
    }
}
