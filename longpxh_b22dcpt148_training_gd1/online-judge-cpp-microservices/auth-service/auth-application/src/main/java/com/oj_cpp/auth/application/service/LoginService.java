package com.oj_cpp.auth.application.service;

import com.oj_cpp.auth.application.dto.request.LoginRequest;
import com.oj_cpp.auth.application.dto.response.AuthResponse;
import com.oj_cpp.auth.domain.model.Role;
import com.oj_cpp.auth.domain.service.PasswordHasher;
import com.oj_cpp.auth.domain.service.RoleService;
import com.oj_cpp.auth.domain.service.TokenService;
import com.oj_cpp.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public AuthResponse login(LoginRequest request) {
        var user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordHasher.check(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        List<String> roles = roleService.findByUserId(user.getId())
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = tokenService.generateToken(user.getUsername(), roles);
        String refreshToken = tokenService.generateRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .build();
    }
}
