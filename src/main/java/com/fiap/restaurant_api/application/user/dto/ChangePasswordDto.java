package com.fiap.restaurant_api.application.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(

        @NotBlank(message = "Current password cannot be empty")
        String currentPassword,

        @NotBlank(message = "New password cannot be empty")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String newPassword

) {
}