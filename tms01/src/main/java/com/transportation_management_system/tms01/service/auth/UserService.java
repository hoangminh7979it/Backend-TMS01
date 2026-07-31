package com.transportation_management_system.tms01.service.auth;

import com.transportation_management_system.tms01.dto.user.*;
import com.transportation_management_system.tms01.entity.auth.User;

import java.util.List;

public interface UserService {

    String DEFAULT_PASSWORD = "Admin@6879";

    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    void changePassword(String username, ChangePasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    boolean isUserActive(User user);

    boolean isWithinWorkingHours(User user);
}
