package com.fiap.restaurant_api.application.user.dto;

public record ChangePasswordDto(

        String currentPassword,
        String newPassword

) {
}