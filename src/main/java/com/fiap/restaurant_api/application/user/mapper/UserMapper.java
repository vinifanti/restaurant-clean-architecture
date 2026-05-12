package com.fiap.restaurant_api.application.user.mapper;

import com.fiap.restaurant_api.application.user.dto.UserResponseDto;
import com.fiap.restaurant_api.domain.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toResponse(User user) {

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getUserType().getName(),
                user.getLastUpdate()
        );
    }
}