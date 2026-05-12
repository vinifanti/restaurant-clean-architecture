package com.fiap.restaurant_api.application.user.dto;

import java.util.UUID;

public record CreateUserDto(

        String name,
        String email,
        String login,
        String password,
        UUID userTypeId

) {
}