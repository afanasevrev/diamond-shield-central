package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.auth.*;
import ru.diamondshield_central.security.CustomUserDetails;
import ru.diamondshield_central.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request,
                               HttpServletRequest httpRequest) {
        return authService.login(request, httpRequest);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return authService.currentUser(userDetails);
    }
}