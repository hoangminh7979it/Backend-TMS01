package com.transportation_management_system.tms01.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UserUpdateRequest {
    private String firstname;
    private String lastname;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;
    private Long roleId;
    private Boolean isActive;

    private LocalTime workStartTime;
    private LocalTime workEndTime;
}
