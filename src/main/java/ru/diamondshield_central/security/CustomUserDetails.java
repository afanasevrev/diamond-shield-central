package ru.diamondshield_central.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.diamondshield_central.entity.SystemUser;

import java.util.Collection;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final SystemUser user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(SystemUser user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public UUID getUserId() {
        return user.getId();
    }

    public SystemUser getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getActive());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}