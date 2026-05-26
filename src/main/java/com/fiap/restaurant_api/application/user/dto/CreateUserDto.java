package com.fiap.restaurant_api.application.user.dto;

public record CreateUserDto(

        String name,
        String email,
        String login,
        String password,
        Long userTypeId

) {
}