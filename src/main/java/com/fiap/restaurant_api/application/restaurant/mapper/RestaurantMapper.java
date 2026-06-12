// application/restaurant/mapper/RestaurantMapper.java
package com.fiap.restaurant_api.application.restaurant.mapper;

import com.fiap.restaurant_api.application.restaurant.dto.RestaurantResponseDto;
import com.fiap.restaurant_api.application.user.mapper.UserMapper;
import com.fiap.restaurant_api.domain.model.Restaurant;

public class RestaurantMapper {

    private RestaurantMapper() {
    }

    public static RestaurantResponseDto toResponse(
            Restaurant restaurant
    ) {

        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHour(),
                restaurant.getClosingHour(),
                UserMapper.toResponse(restaurant.getOwner())
        );
    }
}
