
package com.fiap.restaurant_api.adapters.out.persistence.menuitem;

import com.fiap.restaurant_api.adapters.out.persistence.menuitem.entity.MenuItemJpaEntity;
import com.fiap.restaurant_api.adapters.out.persistence.menuitem.mapper.MenuItemPersistenceMapper;
import com.fiap.restaurant_api.adapters.out.persistence.menuitem.repository.MenuItemJpaRepository;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.MenuItem;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.port.output.MenuItemRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.RestaurantRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemPersistenceAdapter
        implements MenuItemRepositoryPort {

    private final MenuItemJpaRepository menuItemRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public MenuItemPersistenceAdapter(
            MenuItemJpaRepository menuItemRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {

        MenuItemJpaEntity entity =
                MenuItemPersistenceMapper.toEntity(menuItem);

        MenuItemJpaEntity saved =
                menuItemRepository.save(entity);

        Restaurant restaurant =
                resolveRestaurant(saved.getRestaurantId());

        return MenuItemPersistenceMapper.toDomain(saved, restaurant);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {

        return menuItemRepository.findById(id)
                .map(entity -> {
                    Restaurant restaurant =
                            resolveRestaurant(entity.getRestaurantId());
                    return MenuItemPersistenceMapper.toDomain(entity, restaurant);
                });
    }

    @Override
    public List<MenuItem> findAll() {

        return menuItemRepository.findAll()
                .stream()
                .map(entity -> {
                    Restaurant restaurant =
                            resolveRestaurant(entity.getRestaurantId());
                    return MenuItemPersistenceMapper.toDomain(entity, restaurant);
                })
                .toList();
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(entity -> {
                    Restaurant restaurant =
                            resolveRestaurant(entity.getRestaurantId());
                    return MenuItemPersistenceMapper.toDomain(entity, restaurant);
                })
                .toList();
    }

    @Override
    public void delete(MenuItem menuItem) {

        menuItemRepository.delete(
                MenuItemPersistenceMapper.toEntity(menuItem)
        );
    }

    private Restaurant resolveRestaurant(Long restaurantId) {

        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new BusinessException("Restaurant not found"));
    }
}