package com.transportation_management_system.tms01.security;

import com.transportation_management_system.tms01.entity.auth.RolePermission;
import com.transportation_management_system.tms01.entity.auth.User;
import com.transportation_management_system.tms01.repository.auth.RolePermissionRepository;
import com.transportation_management_system.tms01.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndIsDeleteFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));

        List<String> permissionCodes = Collections.emptyList();
        if (user.getRole() != null) {
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRole_RoleIdWithPermission(user.getRole().getRoleId());
            permissionCodes = rolePermissions.stream()
                    .filter(rp -> rp.getPermission() != null)
                    .map(rp -> rp.getPermission().getPermissionCode())
                    .collect(Collectors.toList());
        }

        return new CustomUserDetails(user, permissionCodes);
    }
}
