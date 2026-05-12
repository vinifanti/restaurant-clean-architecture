package com.fiap.restaurant_api.adapters.persistence.user.mapper;

import com.fiap.restaurant_api.adapters.persistence.user.entity.UserJpaEntity;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.model.UserType;

public class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static UserJpaEntity toEntity(User user) {

        UserJpaEntity entity = new UserJpaEntity();

        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setLogin(user.getLogin());
        entity.setPassword(user.getPassword());
        entity.setUserTypeId(
                user.getUserType().getId()
        );
        entity.setLastUpdate(
                user.getLastUpdate()
        );

        return entity;
    }

    public static User toDomain(
            UserJpaEntity entity
    ) {

        UserType userType = new UserType(
                entity.getUserTypeId(),
                null,
                null,
                null
        );

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getPassword(),
                userType,
                entity.getLastUpdate()
        );
    }
}