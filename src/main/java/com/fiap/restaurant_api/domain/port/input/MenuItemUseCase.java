package com.fiap.restaurant_api.domain.port.input;

import com.fiap.restaurant_api.application.menuitem.dto.CreateMenuItemDto;
import com.fiap.restaurant_api.application.menuitem.dto.MenuItemResponseDto;
import com.fiap.restaurant_api.application.menuitem.dto.UpdateMenuItemDto;

import java.util.List;

public interface MenuItemUseCase {

    MenuItemResponseDto create(CreateMenuItemDto dto);

    MenuItemResponseDto findById(Long id);

    List<MenuItemResponseDto> findAll();

    List<MenuItemResponseDto> findByRestaurant(Long restaurantId);

    MenuItemResponseDto update(Long id, UpdateMenuItemDto dto);

    void delete(Long id);
}