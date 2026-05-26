package com.fiap.restaurant_api.application.user.dto;

import java.time.LocalDateTime;

public record UserResponseDto(

        Long id,
        String name,
        String email,
        String login,
        String userType,
        LocalDateTime lastUpdate

) {
}