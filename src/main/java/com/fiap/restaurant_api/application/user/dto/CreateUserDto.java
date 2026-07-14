package com.fiap.restaurant_api.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDto(

        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Login cannot be empty")
        String login,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password,

        @NotNull(message = "User type is required")
        Long userTypeId

) {
}