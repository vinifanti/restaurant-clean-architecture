package com.fiap.restaurant_api.adapters.out.persistence.usertype.mapper;

import com.fiap.restaurant_api.adapters.out.persistence.usertype.entity.UserTypeEntity;
import com.fiap.restaurant_api.domain.model.UserType;

public class UserTypeEntityMapper {

    private UserTypeEntityMapper() {
    }

    public static UserTypeEntity toEntity(
            UserType userType
    ) {

        return new UserTypeEntity(
                userType.getId(),
                userType.getName(),
                userType.getDescription(),
                userType.getLastUpdate()
        );
    }

    public static UserType toDomain(
            UserTypeEntity entity
    ) {

        return new UserType(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLastUpdate()
        );
    }
}