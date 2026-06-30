
package com.fiap.restaurant_api.application.menuitem;

import com.fiap.restaurant_api.application.menuitem.dto.CreateMenuItemDto;
import com.fiap.restaurant_api.application.menuitem.dto.MenuItemResponseDto;
import com.fiap.restaurant_api.application.menuitem.dto.UpdateMenuItemDto;
import com.fiap.restaurant_api.application.menuitem.mapper.MenuItemMapper;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.MenuItem;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.port.input.MenuItemUseCase;
import com.fiap.restaurant_api.domain.port.output.MenuItemRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.RestaurantRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuItemService implements MenuItemUseCase {

    private final MenuItemRepositoryPort menuItemRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public MenuItemService(
            MenuItemRepositoryPort menuItemRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuItemResponseDto create(CreateMenuItemDto dto) {

        Restaurant restaurant = findRestaurant(dto.restaurantId());

        MenuItem menuItem = new MenuItem(
                null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInOnly(),
                dto.imagePath(),
                restaurant
        );

        MenuItem saved = menuItemRepository.save(menuItem);

        return MenuItemMapper.toResponse(saved);
    }

    @Override
    public MenuItemResponseDto findById(Long id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Menu item not found"));

        return MenuItemMapper.toResponse(menuItem);
    }

    @Override
    public List<MenuItemResponseDto> findAll() {

        return menuItemRepository.findAll()
                .stream()
                .map(MenuItemMapper::toResponse)
                .toList();
    }

    @Override
    public List<MenuItemResponseDto> findByRestaurant(Long restaurantId) {

        findRestaurant(restaurantId);

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(MenuItemMapper::toResponse)
                .toList();
    }

    @Override
    public MenuItemResponseDto update(Long id, UpdateMenuItemDto dto) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Menu item not found"));

        menuItem.update(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInOnly(),
                dto.imagePath()
        );

        MenuItem updated = menuItemRepository.save(menuItem);

        return MenuItemMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Menu item not found"));

        menuItemRepository.delete(menuItem);
    }

    private Restaurant findRestaurant(Long restaurantId) {

        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new BusinessException("Restaurant not found"));
    }
}
