package com.transportation_management_system.tms01.service.auth;

import com.transportation_management_system.tms01.dto.auth.LoginRequest;
import com.transportation_management_system.tms01.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(String token);
}
