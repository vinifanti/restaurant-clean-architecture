package com.fiap.restaurant_api.adapters.out.persistence.restaurant;

import com.fiap.restaurant_api.adapters.out.persistence.restaurant.entity.RestaurantJpaEntity;
import com.fiap.restaurant_api.adapters.out.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import com.fiap.restaurant_api.adapters.out.persistence.restaurant.repository.RestaurantJpaRepository;
import com.fiap.restaurant_api.adapters.out.persistence.user.mapper.UserPersistenceMapper;
import com.fiap.restaurant_api.adapters.out.persistence.user.repository.UserJpaRepository;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.port.output.RestaurantRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.UserTypeRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantPersistenceAdapter
        implements RestaurantRepositoryPort {

    private final RestaurantJpaRepository restaurantRepository;
    private final UserJpaRepository userRepository;
    private final UserTypeRepositoryPort userTypeRepository;

    public RestaurantPersistenceAdapter(
            RestaurantJpaRepository restaurantRepository,
            UserJpaRepository userRepository,
            UserTypeRepositoryPort userTypeRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {

        RestaurantJpaEntity entity =
                RestaurantPersistenceMapper.toEntity(restaurant);

        RestaurantJpaEntity saved =
                restaurantRepository.save(entity);

        User owner = resolveOwner(saved.getOwnerId());

        return RestaurantPersistenceMapper.toDomain(saved, owner);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {

        return restaurantRepository.findById(id)
                .map(entity -> {
                    User owner = resolveOwner(entity.getOwnerId());
                    return RestaurantPersistenceMapper.toDomain(entity, owner);
                });
    }

    @Override
    public List<Restaurant> findAll() {

        return restaurantRepository.findAll()
                .stream()
                .map(entity -> {
                    User owner = resolveOwner(entity.getOwnerId());
                    return RestaurantPersistenceMapper.toDomain(entity, owner);
                })
                .toList();
    }

    @Override
    public List<Restaurant> findByName(String name) {

        return restaurantRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(entity -> {
                    User owner = resolveOwner(entity.getOwnerId());
                    return RestaurantPersistenceMapper.toDomain(entity, owner);
                })
                .toList();
    }

    @Override
    public List<Restaurant> findByCuisineType(String cuisineType) {

        return restaurantRepository.findByCuisineTypeIgnoreCase(cuisineType)
                .stream()
                .map(entity -> {
                    User owner = resolveOwner(entity.getOwnerId());
                    return RestaurantPersistenceMapper.toDomain(entity, owner);
                })
                .toList();
    }

    @Override
    public void delete(Restaurant restaurant) {

        restaurantRepository.delete(
                RestaurantPersistenceMapper.toEntity(restaurant)
        );
    }

    private User resolveOwner(Long ownerId) {

        return userRepository.findById(ownerId)
                .map(entity -> {
                    var userType = userTypeRepository
                            .findById(entity.getUserTypeId())
                            .orElseThrow(() ->
                                    new BusinessException("User type not found"));
                    return UserPersistenceMapper.toDomain(entity, userType);
                })
                .orElseThrow(() ->
                        new BusinessException("Owner not found"));
    }
}