package ru.diamondshield_central.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.entity.RolePermission;
import ru.diamondshield_central.entity.SystemUser;
import ru.diamondshield_central.entity.UserRole;
import ru.diamondshield_central.repository.RolePermissionRepository;
import ru.diamondshield_central.repository.SystemUserRepository;
import ru.diamondshield_central.repository.UserRoleRepository;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SystemUserRepository systemUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public CustomUserDetailsService(SystemUserRepository systemUserRepository,
                                    UserRoleRepository userRoleRepository,
                                    RolePermissionRepository rolePermissionRepository) {
        this.systemUserRepository = systemUserRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        SystemUser user = systemUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        List<UserRole> userRoles = userRoleRepository.findBySystemUser(user);
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getCode()));

            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(userRole.getRole().getId());
            for (RolePermission rolePermission : rolePermissions) {
                authorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getCode()));
            }
        }

        return new CustomUserDetails(user, authorities);
    }
}