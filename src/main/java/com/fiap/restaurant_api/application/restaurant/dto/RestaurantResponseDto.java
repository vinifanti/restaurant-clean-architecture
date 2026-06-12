
package com.fiap.restaurant_api.application.restaurant.dto;

import com.fiap.restaurant_api.application.user.dto.UserResponseDto;

import java.time.LocalTime;

public record RestaurantResponseDto(

        Long id,
        String name,
        String address,
        String cuisineType,
        LocalTime openingHour,
        LocalTime closingHour,
        UserResponseDto owner
) {
}