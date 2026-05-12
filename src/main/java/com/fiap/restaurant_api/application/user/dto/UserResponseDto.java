package com.fiap.restaurant_api.application.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(

        UUID id,
        String name,
        String email,
        String login,
        String userType,
        LocalDateTime lastUpdate

) {
}