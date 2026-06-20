package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.auth.*;
import ru.diamondshield_central.entity.SystemUser;
import ru.diamondshield_central.repository.SystemUserRepository;
import ru.diamondshield_central.security.CustomUserDetails;
import ru.diamondshield_central.security.JwtService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SystemUserRepository systemUserRepository;
    private final AuditService auditService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       SystemUserRepository systemUserRepository,
                       AuditService auditService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.systemUserRepository = systemUserRepository;
        this.auditService = auditService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            SystemUser user = userDetails.getUser();
            user.setLastLoginAt(LocalDateTime.now());
            systemUserRepository.save(user);

            List<String> authorities = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String token = jwtService.generateToken(user, authorities);

            auditService.log("LOGIN_SUCCESS", "system_users", user.getId(), null, null, httpRequest);

            return new LoginResponse(token);
        } catch (AuthenticationException ex) {
            auditService.log("LOGIN_FAILED", "system_users", null, null, request.getUsername(), httpRequest);
            throw ex;
        }
    }

    public CurrentUserResponse currentUser(CustomUserDetails userDetails) {
        SystemUser user = userDetails.getUser();

        CurrentUserResponse response = new CurrentUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());

        if (user.getOrganization() != null) {
            response.setOrganizationId(user.getOrganization().getId());
        }

        response.setAuthorities(
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return response;
    }
}