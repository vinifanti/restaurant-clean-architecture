package com.fiap.restaurant_api.application.restaurant;

import com.fiap.restaurant_api.application.restaurant.dto.CreateRestaurantDto;
import com.fiap.restaurant_api.application.restaurant.dto.RestaurantResponseDto;
import com.fiap.restaurant_api.application.restaurant.dto.UpdateRestaurantDto;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.output.RestaurantRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private RestaurantService service;

    private User owner;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        UserType userType = new UserType(1L, "Owner", null, LocalDateTime.now());
        owner = new User(1L, "Mario", "mario@email.com", "mario", "password123", userType, LocalDateTime.now());
        restaurant = new Restaurant(1L, "Cantina", "Rua A, 1", "Italiana",
                LocalTime.of(11, 0), LocalTime.of(23, 0), owner);
    }

    @Test
    void shouldCreateRestaurant() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        RestaurantResponseDto result = service.create(
                new CreateRestaurantDto("Cantina", "Rua A, 1", "Italiana",
                        LocalTime.of(11, 0), LocalTime.of(23, 0), 1L));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Cantina");
        assertThat(result.cuisineType()).isEqualTo("Italiana");
    }

    @Test
    void shouldThrowWhenOwnerNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.create(new CreateRestaurantDto("Cantina", "Rua A, 1", "Italiana",
                        LocalTime.of(11, 0), LocalTime.of(23, 0), 99L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Owner not found");
    }

    @Test
    void shouldFindById() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponseDto result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Cantina");
    }

    @Test
    void shouldThrowWhenRestaurantNotFoundById() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void shouldFindAll() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<RestaurantResponseDto> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Cantina");
    }

    @Test
    void shouldFindByName() {
        when(restaurantRepository.findByName("Cantina")).thenReturn(List.of(restaurant));

        List<RestaurantResponseDto> result = service.findByName("Cantina");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Cantina");
    }

    @Test
    void shouldFindByCuisineType() {
        when(restaurantRepository.findByCuisineType("Italiana")).thenReturn(List.of(restaurant));

        List<RestaurantResponseDto> result = service.findByCuisineType("Italiana");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).cuisineType()).isEqualTo("Italiana");
    }

    @Test
    void shouldUpdateRestaurant() {
        Restaurant updated = new Restaurant(1L, "Trattoria", "Rua B, 2", "Brasileira",
                LocalTime.of(10, 0), LocalTime.of(22, 0), owner);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(updated);

        RestaurantResponseDto result = service.update(1L,
                new UpdateRestaurantDto("Trattoria", "Rua B, 2", "Brasileira",
                        LocalTime.of(10, 0), LocalTime.of(22, 0)));

        assertThat(result.name()).isEqualTo("Trattoria");
        assertThat(result.cuisineType()).isEqualTo("Brasileira");
    }

    @Test
    void shouldThrowWhenUpdatingRestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.update(99L, new UpdateRestaurantDto("X", "Y", "Z",
                        LocalTime.of(10, 0), LocalTime.of(22, 0))))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void shouldDeleteRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        service.delete(1L);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void shouldThrowWhenDeletingRestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Restaurant not found");
    }
}
