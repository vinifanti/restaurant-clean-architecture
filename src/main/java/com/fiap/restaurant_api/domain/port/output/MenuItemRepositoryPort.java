package com.fiap.restaurant_api.domain.port.output;

import com.fiap.restaurant_api.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepositoryPort {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findAll();

    List<MenuItem> findByRestaurantId(Long restaurantId);

    void delete(MenuItem menuItem);
}