package com.example.PayoEat_BE.security.user;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Long roleId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = getRoleNameFromId(roleId);
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    private String getRoleNameFromId(Long roleId) {
        if (roleId == null) return "GUEST";
        switch (roleId.intValue()) {
            case 1: return "CUSTOMER";
            case 2: return "RESTAURANT";
            case 3: return "ADMIN";
            default: return "GUEST";
        }
    }


    public static AuthUserDetails buildUserDetails(User user) {
        return new AuthUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoleId()
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
