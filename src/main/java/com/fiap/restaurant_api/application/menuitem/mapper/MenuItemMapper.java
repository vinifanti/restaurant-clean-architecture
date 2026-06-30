
package com.fiap.restaurant_api.application.menuitem.mapper;

import com.fiap.restaurant_api.application.menuitem.dto.MenuItemResponseDto;
import com.fiap.restaurant_api.application.restaurant.mapper.RestaurantMapper;
import com.fiap.restaurant_api.domain.model.MenuItem;

public class MenuItemMapper {

    private MenuItemMapper() {
    }

    public static MenuItemResponseDto toResponse(MenuItem menuItem) {

        return new MenuItemResponseDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isDineInOnly(),
                menuItem.getImagePath(),
                RestaurantMapper.toResponse(menuItem.getRestaurant())
        );
    }
}