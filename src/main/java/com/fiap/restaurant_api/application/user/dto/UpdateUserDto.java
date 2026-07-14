package com.fiap.restaurant_api.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDto(

        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Login cannot be empty")
        String login

) {
}