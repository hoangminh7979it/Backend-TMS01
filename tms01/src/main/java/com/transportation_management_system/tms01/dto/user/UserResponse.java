package com.transportation_management_system.tms01.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean isActive;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Long roleId;
    private String roleCode;
    private String roleName;
    private LocalDateTime createDate;
}
