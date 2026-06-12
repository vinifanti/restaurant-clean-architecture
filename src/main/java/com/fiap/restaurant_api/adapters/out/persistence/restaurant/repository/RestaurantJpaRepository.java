package com.fiap.restaurant_api.adapters.out.persistence.restaurant.repository;

import com.fiap.restaurant_api.adapters.out.persistence.restaurant.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantJpaRepository
        extends JpaRepository<RestaurantJpaEntity, Long> {

    List<RestaurantJpaEntity> findByNameContainingIgnoreCase(String name);

    List<RestaurantJpaEntity> findByCuisineTypeIgnoreCase(String cuisineType);
}