package com.fiap.restaurant_api.application.usertype.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserTypeResponseDto(
        UUID id,
        String name,
        String description,
        LocalDateTime lastUpdate
) {
}