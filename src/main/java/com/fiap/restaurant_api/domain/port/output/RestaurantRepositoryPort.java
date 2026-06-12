
package com.fiap.restaurant_api.domain.port.output;

import com.fiap.restaurant_api.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryPort {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    List<Restaurant> findByName(String name);

    List<Restaurant> findByCuisineType(String cuisineType);

    void delete(Restaurant restaurant);
}