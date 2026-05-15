package com.fiap.restaurant_api.application.usertype.mapper;

import com.fiap.restaurant_api.application.usertype.dto.UserTypeResponseDto;
import com.fiap.restaurant_api.domain.model.UserType;

public class UserTypeMapper {

    private UserTypeMapper() {
    }

    public static UserTypeResponseDto toResponse(UserType userType) {

        return new UserTypeResponseDto(
                userType.getId(),
                userType.getName(),
                userType.getDescription(),
                userType.getLastUpdate()
        );
    }
}