package com.erp.erp.controller;

import com.erp.erp.Model.User;
import com.erp.erp.dto.AuthResponse;
import com.erp.erp.dto.LoginRequest;
import com.erp.erp.repository.UserRepository;
import com.erp.erp.security.JwtProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();

        // Mapear los roles a un Set de Strings ["ROLE_ADMIN"]
        java.util.Set<String> rolesStr = user.getRoles().stream()
                .map(com.erp.erp.Model.Role::getName)
                .collect(Collectors.toSet());

        // Construir la respuesta exacta requerida por el Frontend
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                rolesStr
        );

        return ResponseEntity.ok(new AuthResponse(jwt, "Bearer", userDto));
    }
}
