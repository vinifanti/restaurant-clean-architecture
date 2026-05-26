package com.fiap.restaurant_api.application.usertype.dto;

import java.time.LocalDateTime;

public record UserTypeResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime lastUpdate
) {
}