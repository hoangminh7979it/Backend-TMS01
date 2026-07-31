package com.transportation_management_system.tms01.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotNull(message = "ID tài khoản không được để trống")
    private Long userId;

    private String customNewPassword; // Null nghĩa là reset về mặc định Admin@6879
}
