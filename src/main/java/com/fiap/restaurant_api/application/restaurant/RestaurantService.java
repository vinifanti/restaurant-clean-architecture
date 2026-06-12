
package com.fiap.restaurant_api.application.restaurant;

import com.fiap.restaurant_api.application.restaurant.dto.CreateRestaurantDto;
import com.fiap.restaurant_api.application.restaurant.dto.RestaurantResponseDto;
import com.fiap.restaurant_api.application.restaurant.dto.UpdateRestaurantDto;
import com.fiap.restaurant_api.application.restaurant.mapper.RestaurantMapper;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.port.input.RestaurantUseCase;
import com.fiap.restaurant_api.domain.port.output.RestaurantRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestaurantService implements RestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepository;
    private final UserRepositoryPort userRepository;

    public RestaurantService(
            RestaurantRepositoryPort restaurantRepository,
            UserRepositoryPort userRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RestaurantResponseDto create(CreateRestaurantDto dto) {

        User owner = findOwner(dto.ownerId());

        Restaurant restaurant = new Restaurant(
                null,
                dto.name(),
                dto.address(),
                dto.cuisineType(),
                dto.openingHour(),
                dto.closingHour(),
                owner
        );

        Restaurant saved = restaurantRepository.save(restaurant);

        return RestaurantMapper.toResponse(saved);
    }

    @Override
    public RestaurantResponseDto findById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Restaurant not found"));

        return RestaurantMapper.toResponse(restaurant);
    }

    @Override
    public List<RestaurantResponseDto> findAll() {

        return restaurantRepository.findAll()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponseDto> findByName(String name) {

        return restaurantRepository.findByName(name)
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();
    }

    @Override
    public List<RestaurantResponseDto> findByCuisineType(String cuisineType) {

        return restaurantRepository.findByCuisineType(cuisineType)
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();
    }

    @Override
    public RestaurantResponseDto update(Long id, UpdateRestaurantDto dto) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Restaurant not found"));

        restaurant.update(
                dto.name(),
                dto.address(),
                dto.cuisineType(),
                dto.openingHour(),
                dto.closingHour()
        );

        Restaurant updated = restaurantRepository.save(restaurant);

        return RestaurantMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Restaurant not found"));

        restaurantRepository.delete(restaurant);
    }

    private User findOwner(Long ownerId) {

        return userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new BusinessException("Owner not found"));
    }
}