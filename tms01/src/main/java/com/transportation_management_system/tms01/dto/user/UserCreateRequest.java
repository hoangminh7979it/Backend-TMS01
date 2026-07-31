package com.transportation_management_system.tms01.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UserCreateRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    private String firstname;
    private String lastname;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;
    private Long roleId;

    private LocalTime workStartTime;
    private LocalTime workEndTime;
}
