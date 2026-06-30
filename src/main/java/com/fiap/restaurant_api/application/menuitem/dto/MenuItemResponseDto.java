
package com.fiap.restaurant_api.application.menuitem.dto;

import com.fiap.restaurant_api.application.restaurant.dto.RestaurantResponseDto;

import java.math.BigDecimal;

public record MenuItemResponseDto(

        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean dineInOnly,
        String imagePath,
        RestaurantResponseDto restaurant
) {
}
