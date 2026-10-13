package com.erp.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType; // Debe ser "Bearer"
    private UserDto user;

    @Data
    @AllArgsConstructor
    public static class UserDto {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private Set<String> roles; // Array de strings ["ROLE_ADMIN"] para tu Frontend
    }
}
