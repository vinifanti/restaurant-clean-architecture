
package com.fiap.restaurant_api.domain.port.input;

import com.fiap.restaurant_api.application.restaurant.dto.CreateRestaurantDto;
import com.fiap.restaurant_api.application.restaurant.dto.RestaurantResponseDto;
import com.fiap.restaurant_api.application.restaurant.dto.UpdateRestaurantDto;

import java.util.List;

public interface RestaurantUseCase {

    RestaurantResponseDto create(CreateRestaurantDto dto);

    RestaurantResponseDto findById(Long id);

    List<RestaurantResponseDto> findAll();

    List<RestaurantResponseDto> findByName(String name);

    List<RestaurantResponseDto> findByCuisineType(String cuisineType);

    RestaurantResponseDto update(Long id, UpdateRestaurantDto dto);

    void delete(Long id);
}