package com.transportation_management_system.tms01.security;

import com.transportation_management_system.tms01.entity.auth.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(User user, List<String> permissionCodes) {
        this.user = user;
        this.authorities = new ArrayList<>();

        // Add Role Code authority (e.g. ROLE_ADMIN, ROLE_USER)
        if (user.getRole() != null && user.getRole().getRoleCode() != null) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleCode()));
        }

        // Add Fine-grained Permission authorities (e.g. USER_READ, SHIPMENT_WRITE)
        if (permissionCodes != null) {
            for (String code : permissionCodes) {
                if (code != null && !code.isBlank()) {
                    this.authorities.add(new SimpleGrantedAuthority(code));
                }
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getIsActive()) && !Boolean.TRUE.equals(user.getIsDelete());
    }
}
