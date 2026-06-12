package com.fiap.restaurant_api.adapters.out.persistence.restaurant.mapper;

import com.fiap.restaurant_api.adapters.out.persistence.restaurant.entity.RestaurantJpaEntity;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.model.User;

public class RestaurantPersistenceMapper {

    private RestaurantPersistenceMapper() {
    }

    public static RestaurantJpaEntity toEntity(Restaurant restaurant) {

        return new RestaurantJpaEntity(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHour(),
                restaurant.getClosingHour(),
                restaurant.getOwner().getId()
        );
    }

    public static Restaurant toDomain(
            RestaurantJpaEntity entity,
            User owner
    ) {

        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCuisineType(),
                entity.getOpeningHour(),
                entity.getClosingHour(),
                owner
        );
    }
}