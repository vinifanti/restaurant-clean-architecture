package com.fiap.restaurant_api.application.user.dto;

public record UpdateUserDto(

        String name,
        String email,
        String login

) {
}